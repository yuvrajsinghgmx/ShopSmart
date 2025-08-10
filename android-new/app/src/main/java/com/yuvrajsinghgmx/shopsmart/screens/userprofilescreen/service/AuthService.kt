package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    ): Flow<AuthState>

    fun verifyOtp(verificationId: String, otp: String): Flow<AuthState>
    fun getLoggedInUser(): String?
}