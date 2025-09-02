package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Shop(
    val shopNumber: String,
    val shopName: String,
    val distance: String,
    val imageUrl: List<String>,
    val category: String,
    val latitude: Double,
    val longitude: Double
)

// Request Body
data class AddShopRequest(
    val name: String,
    val category: String,
    val address: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val image_uploads: List<String>,
    val document_uploads: List<String>
)

data class AddShopResponse(
    val shop_id: String,
    val name: String,
    val category: String,
    val address: String,
    val description: String,
    val is_approved: Boolean,
    val owner_name: String,
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

