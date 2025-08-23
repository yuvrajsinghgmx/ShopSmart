package com.yuvrajsinghgmx.shopsmart.screens.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.modelclass.RatingSummary
import com.yuvrajsinghgmx.shopsmart.modelclass.Review
import com.yuvrajsinghgmx.shopsmart.modelclass.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _ratingSummary = MutableStateFlow<RatingSummary?>(null)
    val summary: StateFlow<RatingSummary?> = _ratingSummary

    var rating by mutableIntStateOf(0)
    var reviewText by mutableStateOf("")

    var selectedTab by mutableStateOf("Most Recent")

    fun onTabSelected(tab: String) {
        selectedTab = tab
        _reviews.value = when (tab) {
            "Most Recent" -> _reviews.value.sortedByDescending { it.createdAt }
            "Most Helpful" -> _reviews.value.sortedByDescending { it.helpfulCount }
            else -> _reviews.value
        }
    }

    fun onRatingSelected(selected: Int) {
        rating = selected
    }

    fun onReviewTextChanged(newText: String) {
        reviewText = newText
    }

    fun loadReviews(target: ReviewTarget) {
        viewModelScope.launch {
            val (summary, reviews) = repository.getReviewsFor(target)
            _reviews.value = reviews.map { review ->
                review.copy(timeAgo = getTimeAgo(review.createdAt))
            }
            _ratingSummary.value = summary
        }
    }

    fun onSubmitReview() {
        //to do

        rating = 0;
        reviewText = ""
    }
}