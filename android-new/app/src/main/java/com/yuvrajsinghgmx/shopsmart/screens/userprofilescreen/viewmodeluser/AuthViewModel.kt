package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.viewmodeluser

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var currentVerificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendInitialOtp(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            repository.sendOtp(phoneNumber, activity, null).collect { state ->
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
                repository.sendOtp(phoneNumber, activity, token).collect { state ->
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
            repository.verifyOtp(verificationId, otp).collect { state ->
                _authState.value = state

            }

            val user = FirebaseAuth.getInstance().currentUser
            user?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        println("🔥 Firebase ID Token: $idToken")
                    } else {
                        task.exception?.printStackTrace()
                    }
                }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}