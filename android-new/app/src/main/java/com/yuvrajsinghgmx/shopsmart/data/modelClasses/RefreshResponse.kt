package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("access") val access: String,
    @SerializedName("refresh") val refresh: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh") val refreshToken: String?
)