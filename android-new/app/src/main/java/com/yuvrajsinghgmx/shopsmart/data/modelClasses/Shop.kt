package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class Shop(
    val shopNumber: Int,
    val shopName: String,
    val distance: String,
    val imageUrl: List<String>,
    val category: String,
    val latitude: Double,
    val longitude: Double
)