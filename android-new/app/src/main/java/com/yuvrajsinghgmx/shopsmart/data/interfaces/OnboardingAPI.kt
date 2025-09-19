package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.OnboardingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface OnboardingAPI {
    @Multipart
    @PUT("onboarding/")
    suspend fun onboardUser(
        @Part("role") role: RequestBody,
        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody?,
        @Part("current_address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("location_radius_km") radius: RequestBody,
        @Part("onboarding_completed") completed: RequestBody,
        @Part profile_image_upload: MultipartBody.Part?
    ): OnboardingResponse
}