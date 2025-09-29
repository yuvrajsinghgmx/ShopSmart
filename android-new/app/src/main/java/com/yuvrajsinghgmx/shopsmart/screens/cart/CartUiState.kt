package com.yuvrajsinghgmx.shopsmart.screens.cart

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Double = 0.0,
    val selectedItemsCount: Int = 0
)
