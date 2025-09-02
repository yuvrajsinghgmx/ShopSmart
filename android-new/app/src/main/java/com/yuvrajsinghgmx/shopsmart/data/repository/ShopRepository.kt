package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import javax.inject.Inject

class ShopRepository @Inject constructor(private val api: ShopApi) {
    suspend fun addShop(request: AddShopRequest): Result<AddShopResponse> {
        return try {
            val response = api.addShop(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getShops(): Result<List<ShopItem>> {
        return try {
            val response = api.getShops()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

