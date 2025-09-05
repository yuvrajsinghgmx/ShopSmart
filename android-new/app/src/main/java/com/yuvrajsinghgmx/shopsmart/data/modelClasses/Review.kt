package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Review(
    val id: Int,
    val username: String,
    val userImage: String?,
    val rating: Int,
    val comment: String,
    val createdAt: String,
    val helpfulCount: Int,
    val timeAgo: String = ""
)

data class RatingSummary(
    val average: Double,
    val totalRatings: Int,
    val distribution: List<Pair<Int, Int>>
)

sealed class ReviewTarget {
    data class Product(val productId: String) : ReviewTarget()
    data class Shop(val shopId: String) : ReviewTarget()
}