package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Shop(
    val id: Int,
    val shopId: String,
    val name: String,
    val images: List<String>,
    val category: String,
    val distance: Double,
    val isFavorite: Boolean,
    val averageRating: Double,
    val description: String,
    val address: String
)

data class ShopResponse(
    val id: Int,
    val shop_id: String,
    val name: String,
    val images: List<String>,
    val address: String,
    val category: String,
    val description: String,
    val is_approved: Boolean,
    val owner_name: String,
    val distance: Double?,
    val shop_type: String,
    val position: Int,
    val is_favorite: Boolean,
    val reviews_count: Int,
    val average_rating: Double,
    val created_at: String
)
