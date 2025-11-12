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
import kotlinx.coroutines.flow.MutableSharedFlow
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

    var isPosting by mutableStateOf(false)
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarEvent.emit(message)
        }
    }

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
            isPosting = true
            try {
                repository.submitReview(target, rating, reviewText)
                val reviewResponse = repository.getReviewsFor(target)
                allReviews = reviewResponse.map { it.toReview() }
                val summary = calculateRatingSummary(allReviews)

                val sorted = when (selectedTab) {
                    "Most Recent" -> allReviews.sortedByDescending { it.createdAt }
                    "Most Helpful" -> allReviews.sortedByDescending { it.helpfulCount }
                    else -> allReviews
                }
                _uiState.value = ReviewUiState.Success(sorted, summary)
                rating = 0;
                reviewText = ""
                showSnackbar("Review posted successfully")

            } catch (e: Exception) {
                showSnackbar( e.message ?: "Something went wrong")
                reviewText = ""
                rating = 0
                Log.v("Error While Posting the review", e.message.toString())
            } finally {
                isPosting = false
            }
        }
    }

    fun toggleHelpful(target: ReviewTarget, reviewId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.toggleHelpful(target, reviewId)
                allReviews = allReviews.map { review ->
                    if (review.id == response.reviewId) {
                        review.copy(
                            helpfulCount = response.helpfulCount,
                            isHelpful = response.isHelpful
                        )
                    } else review
                }
                val sorted = when (selectedTab) {
                    "Most Recent" -> allReviews.sortedByDescending { it.createdAt }
                    "Most Helpful" -> allReviews.sortedByDescending { it.helpfulCount }
                    else -> allReviews
                }
                val currentState = _uiState.value
                val summary =
                    if (currentState is ReviewUiState.Success) currentState.ratingSummary else null
                _uiState.value = ReviewUiState.Success(sorted, summary)
            } catch (e: Exception) {
                showSnackbar(e.message ?: "error marking helpful")
                Log.e("Error while toggling helpful", e.message.toString())
            }
        }
    }
}