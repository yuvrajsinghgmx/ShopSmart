package com.yuvrajsinghgmx.shopsmart.screens.auth.state

import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.DjangoAuthResponse

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : AuthState()
    data class AuthSuccess(
        val djangoAuthResponse: DjangoAuthResponse
    ) : AuthState()

    object firebaseAuthSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}