package com.yuvrajsinghgmx.shopsmart.screens.shared

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.modelclass.User
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SharedAppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val authPrefs: AuthPrefs,
    private val repository: Repository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentVerificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendInitialOtp(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            authRepository.sendOtp(phoneNumber, activity, null).collect { state ->
                if (state is AuthState.CodeSent) {
                    currentVerificationId = state.verificationId
                    forceResendingToken = state.token
                }
                _authState.value = state
            }
        }
    }

    fun resendOtp(phoneNumber: String, activity: Activity) {
        forceResendingToken?.let { token ->
            viewModelScope.launch {
                authRepository.sendOtp(phoneNumber, activity, token).collect { state ->
                    if (state is AuthState.CodeSent) {
                        forceResendingToken = state.token
                    }
                    _authState.value = state
                }
            }
        }
    }

    fun verifyOtp(otp: String) {
        val verificationId = currentVerificationId
        if (verificationId == null) {
            _authState.value = AuthState.Error("Please request an OTP first.")
            return
        }
        viewModelScope.launch {
            authRepository.verifyOtp(verificationId, otp).collect { state ->
                if (state is AuthState.firebaseAuthSuccess) {
                    fetchDjangoToken()
                } else {
                    _authState.value = state
                }
            }
        }
    }

    fun fetchDjangoToken() {
        viewModelScope.launch {
            try {
                val firebaseUser = firebaseAuth.currentUser
                val idToken = firebaseUser?.getIdToken(true)?.await()?.token
                if (idToken != null) {
                    val response = authRepository.getDjangoToken(idToken)
                    Log.d("AuthDebug", "Django response: $response")
                    _authState.value = AuthState.AuthSuccess(response)
                } else {
                    _authState.value = AuthState.Error("Failed to retrieve Firebase ID token.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun getUserData(): User{
        return repository.getUserData()
    }

    fun logout() {
        firebaseAuth.signOut()
        authPrefs.clearAuthData()
        _authState.value = AuthState.Idle
    }
}
