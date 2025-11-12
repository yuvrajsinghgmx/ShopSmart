package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.CartApiService
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddToCartRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CartResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderResponse
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: CartApiService
) {

    suspend fun getCart(): CartResponse = api.getCart()

    suspend fun addToCart(productId: Int, quantity: Int): CartResponse {
        val request = AddToCartRequest(product_id = productId, quantity = quantity)
        val response = api.addToCart(request)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw HttpException(response)
        }
    }

    suspend fun createOrder(shopId: Int, address: String): CreateOrderResponse {
        val body = CreateOrderRequest(
            shopId = shopId,
            paymentMethod = "COD",
            shippingAddress = address
        )
        return api.createOrder(body)
    }

    suspend fun removeFromCart(productId: Int, quantity: Int): CartResponse {
        val response = api.removeFromCart(AddToCartRequest(productId, quantity))
        if (response.isSuccessful) return response.body()!!
        else throw Exception("Remove from cart failed: ${response.code()}")
    }

//    suspend fun clearCart(shopId: Int): Response<CartResponse> {
//        return api.clearCart(shopID)
//    }
}