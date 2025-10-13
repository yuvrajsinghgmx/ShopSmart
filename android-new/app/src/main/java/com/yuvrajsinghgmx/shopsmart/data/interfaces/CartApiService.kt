package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CartResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApiService {
    @GET("orders/cart/")
    suspend fun getCart(): CartResponse

    @POST("orders/cart/")
    suspend fun addToCart(@Query("product_id") productId: String): CartResponse

    @POST("api/orders/create/")
    suspend fun createOrder(@Body body: CreateOrderRequest): CreateOrderResponse
}