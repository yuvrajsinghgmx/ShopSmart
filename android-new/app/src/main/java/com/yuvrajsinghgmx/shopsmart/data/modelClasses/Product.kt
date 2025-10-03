package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Product(
    val id: Int,
    val productId: String,
    val name: String,
    val price: String,
    val category: String,
    val review: String,
    val description: String,
    val imageUrl: List<String>,
    val shopName: String,
    val shopId: String,
    val shopNumber: String,
    val distance: String,
    val isFavorite: Boolean
)