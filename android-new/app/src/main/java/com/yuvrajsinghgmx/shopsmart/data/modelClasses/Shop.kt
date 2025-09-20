package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Shop(
    val shopId: String,
    val shopName: String,
    val shopNumber: String,
    val distance: String,
    val imageUrl: List<String>,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean
)

data class AddShopResponse(
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
    val position: Any?,
    val is_favorite: Boolean,
    val reviews_count: Int,
    val average_rating: Double,
    val created_at: String
)


data class ShopItem(
    val shop_id: String,
    val name: String,
    val category: String,
    val address: String,
    val is_approved: Boolean,
    val owner_name: String,
    val distance: Double,
    val is_favorite: Boolean
)

