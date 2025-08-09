package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service

import android.app.Activity
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthState> // Add Activity
    fun verifyOtp(verificationId: String, otp: String): Flow<AuthState>
    fun getLoggedInUser(): String?
}
