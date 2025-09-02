package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShopApi {
    @POST("api/shops/")
    suspend fun addShop(
        @Body request: AddShopRequest
    ): Response<AddShopResponse>

    @GET("api/shops/")
    suspend fun getShops(): Response<List<ShopItem>>
}