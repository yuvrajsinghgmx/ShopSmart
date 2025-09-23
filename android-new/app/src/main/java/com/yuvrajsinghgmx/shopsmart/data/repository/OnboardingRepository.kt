package com.yuvrajsinghgmx.shopsmart.data.repository

import android.util.Log
import com.yuvrajsinghgmx.shopsmart.data.interfaces.OnboardingAPI
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.OnboardingResponse
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.UserRole
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    private val api: OnboardingAPI,
    private val authPrefs: AuthPrefs
) {
    suspend fun completeOnboarding(
        role: String,
        fullName: String,
        address: String,
        latitude: Double,
        longitude: Double,
        radius: Int,
        imageFile: File?,
        email: String? = null
    ): OnboardingResponse {
        // text fields
        val roleBody = role.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val latBody = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lonBody = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val completedBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val radiusBody = radius.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email?.takeIf { it.isNotBlank() }
            ?.toRequestBody("text/plain".toMediaTypeOrNull())

        // image
        val imagePart = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profile_image_upload", it.name, reqFile)
        }

        val response = api.onboardUser(
            role = roleBody,
            fullName = nameBody,
            email = emailBody,
            address = addressBody,
            latitude = latBody,
            longitude = lonBody,
            radius = radiusBody,
            profile_image_upload = imagePart,
            completed = completedBody
        )

        try {
            if (response.onboardingCompleted){
                authPrefs.saveAuthData(
                    accessToken = authPrefs.getAccessToken() ?: "",
                    refreshToken = authPrefs.getRefreshToken() ?: "",
                    userId = authPrefs.getUserId(),
                    name = fullName,
                    phone = authPrefs.getPhone(),
                    profilePic = response.profileImage,
                    isNewUser = false,
                    role = role,
                    isOnboardingCompleted = true
                )
            }
        } catch (e: HttpException) {
            Log.e("Onboarding", "Error: ${e.code()} ${e.response()?.errorBody()?.string()}")
            throw e
        } catch (e: Exception) {
            Log.e("Onboarding", "Unexpected error: ${e.localizedMessage}")
            throw e
        }
        return response
    }
}
