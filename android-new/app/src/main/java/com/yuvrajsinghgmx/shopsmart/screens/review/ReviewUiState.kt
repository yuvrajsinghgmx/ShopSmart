package com.yuvrajsinghgmx.shopsmart.screens.review

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RatingSummary
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Review

sealed class ReviewUiState {
    object Loading: ReviewUiState()
    data class Success(val reviews:List<Review>, val ratingSummary: RatingSummary?): ReviewUiState()
    data class Error(val message:String): ReviewUiState()
}