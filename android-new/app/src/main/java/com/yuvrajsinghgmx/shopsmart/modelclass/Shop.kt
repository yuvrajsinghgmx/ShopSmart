package com.yuvrajsinghgmx.shopsmart.modelclass

data class Shop(
    val shopNumber: Int,
    val shopName: String,
    val distance: String,
    val imageUrl: List<String>,
    val category: String
)
