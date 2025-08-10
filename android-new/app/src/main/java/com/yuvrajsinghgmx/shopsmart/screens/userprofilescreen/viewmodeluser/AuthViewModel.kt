package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.viewmodeluser

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel // Example with Hilt
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // We need to store the verificationId between sending the OTP and verifying it
    private var currentVerificationId: String? = null

    fun sendOtp(phoneNumber: String, activity: Activity) { // Add Activity
        viewModelScope.launch {
            repository.sendOtp(phoneNumber, activity).collect { state -> // Pass it down
                if (state is AuthState.CodeSent) {
                    currentVerificationId = state.verificationId
                }
                _authState.value = state
            }
        }
    }

    fun verifyOtp(otp: String) {
        val verificationId = currentVerificationId
        if (verificationId == null) {
            _authState.value = AuthState.Error("Verification ID not found. Please request a new OTP.")
            return
        }

        viewModelScope.launch {
            repository.verifyOtp(verificationId, otp).collect { state ->
                _authState.value = state
            }
        }
    }

    // Optional: Reset state if user navigates away and back
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}