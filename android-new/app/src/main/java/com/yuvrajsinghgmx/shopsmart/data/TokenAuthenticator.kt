package com.yuvrajsinghgmx.shopsmart.data

import android.util.Log
import com.yuvrajsinghgmx.shopsmart.data.interfaces.DjangoAuthApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.RefreshApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshTokenRequest
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPrefs: AuthPrefs,
    private val refreshApi: RefreshApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        val refreshToken = authPrefs.getRefreshToken() ?: return null

        return try {
            val newTokens = runBlocking {
                refreshApi.refreshAccessToken(RefreshTokenRequest(refreshToken))
            }

            if (newTokens.access.isNotBlank()) {
                // Save tokens
                authPrefs.saveAuthData(
                    accessToken = newTokens.access,
                    refreshToken = newTokens.refresh,
                    userId = authPrefs.getUserId(),
                    name = authPrefs.getName(),
                    profilePic = authPrefs.getProfilePic(),
                    phone = authPrefs.getPhone(),
                    isNewUser = authPrefs.isNewUser(),
                    role = authPrefs.getRole(),
                    isOnboardingCompleted = authPrefs.isOnboarded()
                )

                // Retry request with **new access token**
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.access}")
                    .build()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthDebug", "Token refresh failed: ${e.message}")
            null
        }
    }

    // Count prior responses to avoid retry loops
    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}