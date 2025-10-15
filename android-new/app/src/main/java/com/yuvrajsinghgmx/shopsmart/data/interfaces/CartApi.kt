package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddToCartRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CartResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface CartApiService {

    @GET("orders/cart/")
    suspend fun getCart(): CartResponse

    @POST("orders/cart/")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<CartResponse>

    @POST("orders/create/")
    suspend fun createOrder(@Body body: CreateOrderRequest): CreateOrderResponse

    @HTTP(method = "DELETE", path = "orders/cart/", hasBody = true)
    suspend fun removeFromCart(@Body request: AddToCartRequest): Response<CartResponse>

//    @DELETE("orders/cart/")
//    suspend fun clearCart(shopID: shopID): Response<CartResponse>
}