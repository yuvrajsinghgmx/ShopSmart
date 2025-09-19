package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("auth/refresh/")
    suspend fun refreshAccessToken(@Body body: RefreshTokenRequest): RefreshResponse
}