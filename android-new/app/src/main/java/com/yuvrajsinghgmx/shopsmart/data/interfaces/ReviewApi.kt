package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {

    @POST("shops/{shop_id}/reviews/")
    suspend fun postShopReview(
        @Path("shop_id") shopId: String,
        @Body request: ReviewRequest
    ): ReviewResponse

    @POST("products/{product_id}/reviews/")
    suspend fun postProductReview(
        @Path("product_id") productId: String,
        @Body request: ReviewRequest
    ): ReviewResponse

    @GET("shops/{shop_id}/get/reviews/")
    suspend fun getShopReviews(
        @Path("shop_id") shopId : String
    ): List<ReviewResponse>

    @GET("products/{product_id}/get/reviews/")
    suspend fun getProductReviews(
        @Path("product_id") productId: String
    ): List<ReviewResponse>

    @POST("products/{product_id}/reviews/{product_review_id}/toggle-helpful/")
    suspend fun toggleProductReviewHelpful(
        @Path("product_id") productId: String,
        @Path("product_review_id") reviewId: Int
    ): ToggleReviewResponse

    @POST("shops/{shop_id}/reviews/{shop_review_id}/toggle-helpful/")
    suspend fun toggleShopReviewHelpful(
        @Path("shop_id") shopId: String,
        @Path("shop_review_id") reviewId: Int
    ): ToggleReviewResponse
}