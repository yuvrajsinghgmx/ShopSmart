package com.yuvrajsinghgmx.shopsmart.screens.review

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Review
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.toReview
import com.yuvrajsinghgmx.shopsmart.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: ReviewRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState


    var rating by mutableIntStateOf(0)
    var reviewText by mutableStateOf("")

    var selectedTab by mutableStateOf("Most Recent")

    private var allReviews: List<Review> = emptyList()

    fun onTabSelected(tab: String) {
        selectedTab = tab
        val sorted = when (tab) {
            "Most Recent" -> allReviews.sortedByDescending { it.createdAt }
            "Most Helpful" -> allReviews.sortedByDescending { it.helpfulCount }
            else -> allReviews
        }
        val summary = if (allReviews.isNotEmpty()) calculateRatingSummary(allReviews) else null
        _uiState.value = ReviewUiState.Success(sorted, summary)
    }

    fun onRatingSelected(selected: Int) {
        rating = selected
    }

    fun onReviewTextChanged(newText: String) {
        reviewText = newText
    }

    fun loadReviews(target: ReviewTarget) {
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            try {
                val reviewResponse = repository.getReviewsFor(target)
                allReviews = reviewResponse.map { it.toReview() }
                val summary = calculateRatingSummary(allReviews)

                val sorted = when (selectedTab) {
                    "Most Recent" -> allReviews.sortedByDescending { it.createdAt }
                    "Most Helpful" -> allReviews.sortedByDescending { it.helpfulCount }
                    else -> allReviews
                }
                _uiState.value = ReviewUiState.Success(sorted, summary)

            } catch (e: Exception) {
                _uiState.value = ReviewUiState.Error(e.message ?: "Something went wrong!")
            }
        }
    }

    fun onSubmitReview(target: ReviewTarget) {
        viewModelScope.launch {
            try {
                repository.submitReview(target, rating, reviewText)
                loadReviews(target)
                rating = 0;
                reviewText = ""

            } catch (e: Exception) {
                Log.v("Error While Posting the review", e.message.toString())
            }
        }
    }
    fun toggleHelpful(target: ReviewTarget,reviewId:Int){
        viewModelScope.launch {
            try{
                val response = repository.toggleHelpful(target,reviewId)
                allReviews = allReviews.map{ review ->
                    if(review.id == response.reviewId){
                        review.copy(
                            helpfulCount = response.helpfulCount,
                            isHelpful = response.isHelpful
                        )
                    }else review
                }
                val sorted = when(selectedTab){
                    "Most Recent" -> allReviews.sortedByDescending { it.createdAt }
                    "Most Helpful" -> allReviews.sortedByDescending { it.helpfulCount }
                    else -> allReviews
                }
                val currentState = _uiState.value
                val summary = if(currentState is ReviewUiState.Success) currentState.ratingSummary else null
                _uiState.value = ReviewUiState.Success(sorted, summary)
            }catch (e: Exception){
                Log.e("Error while toggling helpful",e.message.toString())
            }
        }
    }
}