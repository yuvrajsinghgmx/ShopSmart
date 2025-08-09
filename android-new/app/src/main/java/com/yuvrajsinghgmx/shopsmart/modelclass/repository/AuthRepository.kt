package com.yuvrajsinghgmx.shopsmart.modelclass.repository

import android.app.Activity
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service.AuthService
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthState>
    fun verifyOtp(verificationId: String, otp: String): Flow<AuthState>
    fun getCurrentUserPhone(): String?
}

// Implementation
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthState> { // Add Activity
        return authService.sendOtp(phoneNumber, activity) // Pass it down
    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthState> {
        return authService.verifyOtp(verificationId, otp)
    }

    override fun getCurrentUserPhone(): String? {
        return authService.getLoggedInUser()
    }
}