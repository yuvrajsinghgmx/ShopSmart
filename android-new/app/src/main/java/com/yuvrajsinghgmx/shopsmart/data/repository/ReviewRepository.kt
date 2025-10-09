package com.yuvrajsinghgmx.shopsmart.data.repository

import com.google.gson.JsonParser
import com.yuvrajsinghgmx.shopsmart.data.interfaces.ReviewApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleReviewResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val reviewApi: ReviewApi) {
    suspend fun submitReview(target: ReviewTarget, rating: Int, comment: String): ReviewResponse {
        val request = ReviewRequest(rating, comment)
        try{
            return when (target) {
                is ReviewTarget.Product -> reviewApi.postProductReview(target.productId, request)
                is ReviewTarget.Shop -> reviewApi.postShopReview(target.shopId, request)
            }
        }catch (e: HttpException){
            val errorMsg = parseError(e.response()?.errorBody())
            throw Exception(errorMsg)
        }catch (e: Exception){
            throw Exception("Something went wrong")
        }
    }

    suspend fun getReviewsFor(target: ReviewTarget): List<ReviewResponse> {
        return when (target) {
            is ReviewTarget.Product -> reviewApi.getProductReviews(target.productId)
            is ReviewTarget.Shop -> reviewApi.getShopReviews(target.shopId)
        }
    }

    suspend fun toggleHelpful(target: ReviewTarget, reviewId: Int): ToggleReviewResponse {
        try{
            return when (target) {
                is ReviewTarget.Product -> reviewApi.toggleProductReviewHelpful(target.productId, reviewId)
                is ReviewTarget.Shop -> reviewApi.toggleShopReviewHelpful(target.shopId, reviewId)
            }
        }catch (e: HttpException){
            val errorMsg = parseError(e.response()?.errorBody())
            throw Exception(errorMsg)
        }catch (e: Exception){
            throw Exception("Something went wrong")
        }
    }
    private fun parseError(errorBody: ResponseBody?): String{
        return try{
            val jsonElement = JsonParser.parseString(errorBody?.string()?:"{}")
            val jsonObject = jsonElement.asJsonObject
            if(jsonObject.has("error"))
                jsonObject["error"].asString
            else
                "Something went wrong"
        } catch (e: Exception) {
            "Unexpected server error"
        }
    }
}