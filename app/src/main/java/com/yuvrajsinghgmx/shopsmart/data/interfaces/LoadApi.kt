package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.HomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface LoadApi {
    @GET("load/")
    suspend fun getLoadData(): Response<HomeResponse>
}