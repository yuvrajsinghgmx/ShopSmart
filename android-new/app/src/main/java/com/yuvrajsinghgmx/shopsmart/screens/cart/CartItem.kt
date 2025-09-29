package com.yuvrajsinghgmx.shopsmart.screens.cart

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val isSelected: Boolean = true
)
