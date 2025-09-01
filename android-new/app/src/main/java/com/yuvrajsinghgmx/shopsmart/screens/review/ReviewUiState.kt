package com.yuvrajsinghgmx.shopsmart.screens.review

import com.yuvrajsinghgmx.shopsmart.modelclass.RatingSummary
import com.yuvrajsinghgmx.shopsmart.modelclass.Review

sealed class ReviewUiState {
    object Loading: ReviewUiState()
    data class Success(val reviews:List<Review> , val ratingSummary: RatingSummary?): ReviewUiState()
    data class Error(val message:String): ReviewUiState()
}