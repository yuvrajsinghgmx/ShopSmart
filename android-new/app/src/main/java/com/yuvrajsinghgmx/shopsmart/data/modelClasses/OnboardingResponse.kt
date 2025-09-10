package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    val role: String,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("profile_image")
    val profileImage: String?,
    @SerializedName("current_address")
    val currentAddress: String?,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName("location_radius_km")
    val locationRadiusKm: Int?,
    @SerializedName("onboarding_completed")
    val onboardingCompleted: Boolean,
    val email: String?
)
