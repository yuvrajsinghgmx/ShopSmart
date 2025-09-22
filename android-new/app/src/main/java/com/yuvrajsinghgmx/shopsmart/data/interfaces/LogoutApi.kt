package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.LogoutRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.LogoutResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LogoutApi {
    @POST("auth/logout/")
    suspend fun logout(
        @Body refresh: LogoutRequest
    ): LogoutResponse
}