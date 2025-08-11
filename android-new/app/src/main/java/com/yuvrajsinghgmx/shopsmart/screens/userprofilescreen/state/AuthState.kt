package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state

import com.google.firebase.auth.PhoneAuthProvider

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : AuthState()
    object AuthSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}