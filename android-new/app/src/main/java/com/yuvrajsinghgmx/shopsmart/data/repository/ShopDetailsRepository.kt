package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopDetailsApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
import javax.inject.Inject

class ShopDetailsRepository @Inject constructor(
    private val api: ShopDetailsApi
) {
    suspend fun getShopDetails(shopId: Int): Result<ShopResponse> {
        return try {
            val response = api.getShopDetails(shopId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch shop details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}