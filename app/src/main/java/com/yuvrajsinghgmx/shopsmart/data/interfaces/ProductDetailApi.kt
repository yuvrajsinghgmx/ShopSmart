package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ProductDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductDetailApi {
    @GET("products/{id}/")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): Response<ProductDetailResponse>
}