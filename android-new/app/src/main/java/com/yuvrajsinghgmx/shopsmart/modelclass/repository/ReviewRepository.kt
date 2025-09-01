package com.yuvrajsinghgmx.shopsmart.modelclass.repository

import com.yuvrajsinghgmx.shopsmart.api.ReviewApi
import com.yuvrajsinghgmx.shopsmart.modelclass.ReviewResponse
import com.yuvrajsinghgmx.shopsmart.modelclass.ReviewRequest
import com.yuvrajsinghgmx.shopsmart.modelclass.ReviewTarget
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
}