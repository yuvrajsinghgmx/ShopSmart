package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class CartResponse(
    val id: Int,
    val user: Int,
    val items: List<CartItem>,
    @SerializedName("total_cart_value") val totalCartValue: Double
)

data class CartItem(
    val id: Int,
    val product: Int,
    val product_name: String,
    val price: String,
    val quantity: Int,
    val shop_id: Int,
    val shop_name: String,
    val product_images: List<String>
)

data class CreateOrderRequest(
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("shipping_address") val shippingAddress: String
)

data class CreateOrderResponse(
    val payment_method: String,
    val shipping_address: String
)

data class AddToCartRequest(
    val product_id: Int,
    val quantity: Int
)