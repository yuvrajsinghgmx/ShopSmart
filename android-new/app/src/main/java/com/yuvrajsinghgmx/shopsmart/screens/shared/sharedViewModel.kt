package com.yuvrajsinghgmx.shopsmart.screens.shared

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.User
import com.yuvrajsinghgmx.shopsmart.data.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.OnboardingRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.UserRepository
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.UserRole
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.File

@HiltViewModel
class SharedAppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
    val authPrefs: AuthPrefs,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState
    val authState: StateFlow<AuthState> = _authState
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentVerificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private val _isOnboarding = MutableStateFlow(false)
    val isOnboarding: StateFlow<Boolean> = _isOnboarding
    
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
                    if (response.access.isBlank() || response.refresh.isBlank()) {
                        Log.e("AuthDebug", "Missing tokens in response: $response")
                        _authState.value = AuthState.Error("Login failed: missing tokens")
                    } else {
                        _authState.value = AuthState.AuthSuccess(response)
                    }
                } else {
                    _authState.value = AuthState.Error("Failed to retrieve Firebase ID token.")
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    Log.e(
                        "AuthDebug",
                        "HttpException: ${e.code()} - ${e.response()?.errorBody()?.string()}"
                    )
                }
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun getLogInData() {
        _userState.value=userRepository.getLoggedInUser()
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try{

                val beforeToken = authPrefs.getRefreshToken()
                Log.d("AuthDebug", "Before logout refreshToken = $beforeToken")

                firebaseAuth.signOut()
                val refresh = authPrefs.getRefreshToken()
                authRepository.logout(refresh)

                val afterToken = authPrefs.getRefreshToken()
                Log.d("AuthDebug", "After logout refreshToken = $afterToken")

                _authState.value = AuthState.Idle
            }catch (e: Exception){
                _authState.value = AuthState.Error(e.message?:"Logout Failed")
            }
        }
    }

    fun completeOnboarding(
        role: String,
        fullName: String,
        address: String,
        latitude: Double,
        longitude: Double,
        radius: Int,
        imageFile: File?,
        email: String? = null
    ) {
        viewModelScope.launch {
            try {
                _isOnboarding.value = true
                val response = onboardingRepository.completeOnboarding(
                    role = role,
                    fullName = fullName,
                    address = address,
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                    imageFile = imageFile,
                    email = email
                )
                Log.d("AuthDebug", "Onboarding response: $response")
                if (response.onboardingCompleted){
                    val roleEnum = UserRole.valueOf(role.uppercase())
                    _authState.value = AuthState.onboardingSuccess(roleEnum)
                    _userState.value = userRepository.getLoggedInUser()
                }
            } catch (e: Exception) {
                Log.e("AuthDebug", "Error completing onboarding: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }finally {
                _isOnboarding.value = false
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            val user = authRepository.getUser()
            _userState.value = user
        }
    }
}
