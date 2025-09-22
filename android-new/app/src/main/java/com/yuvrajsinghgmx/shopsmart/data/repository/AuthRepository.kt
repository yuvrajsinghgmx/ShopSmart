package com.yuvrajsinghgmx.shopsmart.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.PhoneAuthProvider
import com.yuvrajsinghgmx.shopsmart.data.interfaces.DjangoAuthApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.LogoutApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.RefreshApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.DjangoAuthResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.FirebaseIdTokenRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.LogoutRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.LogoutResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshTokenRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.User
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
    suspend fun refreshToken(refreshToken: String?): RefreshResponse
    suspend fun getUser(): User?
    suspend fun logout(refresh: String?): LogoutResponse
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val djangoApi: DjangoAuthApi,
    private val refreshApi: RefreshApi,
    private val logoutApi: LogoutApi,
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
                userId = resp.user.id,
                name = resp.user.name,
                phone = resp.user.phoneNumber,
                profilePic = resp.user.profilePic,
                isNewUser = resp.user.isNewUser,
                role = resp.user.role
            )
            resp
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthDebug", "Auth API failed: ${e.code()} $errorBody")
            throw e
        }
    }

    override suspend fun refreshToken(refreshToken: String?): RefreshResponse {
        return try {
            val body = RefreshTokenRequest(refreshToken ?: "")
            val resp = refreshApi.refreshAccessToken(body)
            resp
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthDebug", "Refresh API failed: ${e.code()} $errorBody")
            throw e
        }
    }

    override suspend fun getUser(): User? {
        return sharedPrefs.getUser()
    }

    override suspend fun logout(refresh: String?): LogoutResponse {
        return try {
            val body = LogoutRequest(refresh ?: "")
            val response = logoutApi.logout(body)

            sharedPrefs.clearAuthData()
            response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthDebug", "Logout API failed:${e.code()} $errorBody")
            throw e
        }
    }
}