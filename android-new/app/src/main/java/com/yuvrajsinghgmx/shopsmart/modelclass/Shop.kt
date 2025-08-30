package com.yuvrajsinghgmx.shopsmart.modelclass

data class Shop(
    val shopNumber: String,
    val shopName: String,
    val distance: String,
    val imageUrl: List<String>,
    val category: String,
    val latitude: Double,
    val longitude: Double
)
