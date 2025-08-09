package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class CodeSent(val verificationId: String) : AuthState()
    object AuthSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}