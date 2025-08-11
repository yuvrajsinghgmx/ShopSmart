package com.yuvrajsinghgmx.shopsmart.modelclass.repository

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service.AuthService
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        token: PhoneAuthProvider.ForceResendingToken? = null
    ): Flow<AuthState>

    fun verifyOtp(verificationId: String, otp: String): Flow<AuthState>
    fun getCurrentUserPhone(): String?
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {
    override fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        token: PhoneAuthProvider.ForceResendingToken?
    ): Flow<AuthState> {
        return authService.sendOtp(phoneNumber, activity, token)
    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthState> {
        return authService.verifyOtp(verificationId, otp)
    }

    override fun getCurrentUserPhone(): String? {
        return authService.getLoggedInUser()
    }
}
