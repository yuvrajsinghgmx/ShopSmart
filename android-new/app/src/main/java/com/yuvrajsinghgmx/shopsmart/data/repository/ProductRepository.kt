package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ProductDetailApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ProductDetailResponse
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ProductDetailApi) {
    suspend fun getProductDetails(productId: Int): Result<ProductDetailResponse> {
        return try {
            val response = api.getProductDetails(productId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}