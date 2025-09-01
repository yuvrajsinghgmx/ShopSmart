package com.yuvrajsinghgmx.shopsmart.api

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {

    @POST("api/shops/{shop_id}/reviews/")
    suspend fun postShopReview(
        @Path("shop_id") shopId: String,
        @Body request: ReviewRequest
    ): ReviewResponse

    @POST("api/products/{product_id}/reviews/")
    suspend fun postProductReview(
        @Path("product_id") productId: String,
        @Body request: ReviewRequest
    ): ReviewResponse

    @GET("api/shops/{shop_id}/get/reviews/")
    suspend fun getShopReviews(
        @Path("shop_id") shopId : String
    ): List<ReviewResponse>

    @GET("/api/products/{product_id}/get/reviews/")
    suspend fun getProductReviews(
        @Path("product_id") productId: String
    ): List<ReviewResponse>
}