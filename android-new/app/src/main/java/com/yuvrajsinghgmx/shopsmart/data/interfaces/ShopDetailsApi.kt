package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopDetailsApi {
    @GET("shops/{id}/")
    suspend fun getShopDetails(
        @Path("id") id : Int
    ): Response<ShopResponse>
}