package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ShopApi {
/*    @Multipart
    @POST("api/shops/")
    suspend fun addShop(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("address") address: RequestBody,
        @Part("description") description: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("shop_type") shopType: RequestBody,
        @Part("position") position: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Part documents: List<MultipartBody.Part>
    ): Response<AddShopResponse>*/

    @POST("api/shops/")
    suspend fun addShop(
        @Body shopRequest: ShopRequest
    ): Response<AddShopResponse>

    @GET("api/shops/")
    suspend fun getShops(): Response<List<ShopItem>>
}