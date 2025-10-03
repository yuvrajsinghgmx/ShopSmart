package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ShopApi {
    @Multipart
    @POST("shops/")
    suspend fun addShop(
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("shop_type") shopType: RequestBody,
        @Part("position") position: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part imageUploads: List<MultipartBody.Part>,      // Multiple images
        @Part documentUploads: List<MultipartBody.Part>    // Multiple documents
    ): Response<ShopResponse>
}