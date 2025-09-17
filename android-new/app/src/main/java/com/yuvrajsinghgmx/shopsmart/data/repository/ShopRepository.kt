package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ShopRepository @Inject constructor(private val api: ShopApi) {
/*    suspend fun addShop(
        name: RequestBody,
        category: RequestBody,
        address: RequestBody,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        shopType: RequestBody,
        position: RequestBody,
        images: List<MultipartBody.Part>,
        documents: List<MultipartBody.Part>
    ): Result<AddShopResponse> {
        return try {
            val response = api.addShop(
                name, category, address, description,
                latitude, longitude,shopType,position,
                images, documents
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }*/

    suspend fun addShop(shopRequest: ShopRequest): Result<AddShopResponse> {
        return try {
            val response = api.addShop(shopRequest)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
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

