package com.yuvrajsinghgmx.shopsmart.screens.cart

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int = 1
)
