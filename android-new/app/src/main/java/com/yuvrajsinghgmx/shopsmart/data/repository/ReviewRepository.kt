package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ReviewApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleReviewResponse
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val reviewApi : ReviewApi){
    suspend fun submitReview(target: ReviewTarget, rating:Int, comment:String): ReviewResponse{
        val request = ReviewRequest(rating,comment)
        return when(target){
            is ReviewTarget.Product -> reviewApi.postProductReview(target.productId,request)
            is ReviewTarget.Shop -> reviewApi.postShopReview(target.shopId,request)
        }
    }

    suspend fun getReviewsFor(target: ReviewTarget): List<ReviewResponse> {
        return when (target) {
            is ReviewTarget.Product -> reviewApi.getProductReviews(target.productId)
            is ReviewTarget.Shop -> reviewApi.getShopReviews(target.shopId)
        }
    }
    suspend fun toggleHelpful(target: ReviewTarget, reviewId: Int): ToggleReviewResponse {
        return when (target) {
            is ReviewTarget.Product -> reviewApi.toggleProductReviewHelpful(target.productId,reviewId)
            is ReviewTarget.Shop -> reviewApi.toggleShopReviewHelpful(target.shopId,reviewId)
        }
    }
}