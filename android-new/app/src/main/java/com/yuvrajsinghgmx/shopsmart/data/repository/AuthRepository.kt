package com.yuvrajsinghgmx.shopsmart.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.data.interfaces.DjangoAuthApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.DjangoAuthResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.FirebaseIdTokenRequest
import com.yuvrajsinghgmx.shopsmart.screens.auth.service.AuthService
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

interface AuthRepository {
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        token: PhoneAuthProvider.ForceResendingToken? = null
    ): Flow<AuthState>

    fun verifyOtp(verificationId: String, otp: String): Flow<AuthState>
    fun getCurrentUserPhone(): String?

    suspend fun getDjangoToken(firebaseIdToken: String?): DjangoAuthResponse
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val djangoApi: DjangoAuthApi,
    private val sharedPrefs: AuthPrefs
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

    override suspend fun getDjangoToken(firebaseIdToken: String?): DjangoAuthResponse {
        return try {
            val body = FirebaseIdTokenRequest(firebaseIdToken)
            val resp = djangoApi.exchangeFirebaseToken(body)
            sharedPrefs.saveAuthData(
                accessToken = resp.access,
                refreshToken = resp.refresh,
                userId = resp.user?.id,
                name = resp.user?.name,
                phone = resp.user?.phoneNumber,
                profilePic = resp.user?.profilePic,
                isNewUser = resp.user?.isNewUser == true
            )
            resp
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthDebug", "Auth API failed: ${e.code()} $errorBody")
            throw e
        }
    }
}
