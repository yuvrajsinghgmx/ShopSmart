package com.yuvrajsinghgmx.shopsmart.modelclass

data class Review(
    val id: String,
    val userName: String,
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
    val distribution: Map<Int, Int>
)

sealed class ReviewTarget {
    data class Product(val productId: String, val name: String) : ReviewTarget()
    data class Shop(val shopId: String, val name: String) : ReviewTarget()
}