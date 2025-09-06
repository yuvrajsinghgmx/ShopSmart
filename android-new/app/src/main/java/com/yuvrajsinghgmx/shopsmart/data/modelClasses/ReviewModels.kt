package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName
import com.yuvrajsinghgmx.shopsmart.screens.review.getTimeAgo

data class ReviewRequest(
    val rating: Int,
    val comment: String
)

data class ReviewResponse(
    val id: Int,
    val rating: Int,
    val comment: String,
    @SerializedName("user_name")
    val username: String,
    @SerializedName("user_image")
    val userImage: String,
    @SerializedName("created_at")
    val createdAt: String,
    val helpfulCount: Int
)

fun ReviewResponse.toReview(): Review {
    return Review(
        id = id,
        username = username,
        userImage = userImage,
        rating = rating,
        comment = comment,
        createdAt = createdAt,
        helpfulCount = helpfulCount,
        timeAgo = getTimeAgo(createdAt)
    )
}