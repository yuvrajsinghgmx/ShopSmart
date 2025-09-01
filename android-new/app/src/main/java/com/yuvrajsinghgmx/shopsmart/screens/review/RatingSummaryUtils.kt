package com.yuvrajsinghgmx.shopsmart.screens.review

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RatingSummary
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Review

fun calculateRatingSummary(reviews: List<Review>): RatingSummary {
    if (reviews.isEmpty()) {
        return RatingSummary(0.0, 0, emptyList())
    }
    val totalRatings = reviews.size
    val averageRating = reviews.map { it.rating }.average()
    val distribution = reviews.groupingBy { it.rating }.eachCount()
        .let { counts ->
            (1..5).map { star -> star to (counts[star] ?: 0) }

        }
        .sortedByDescending { it.first }
    return RatingSummary(averageRating, totalRatings, distribution)
}