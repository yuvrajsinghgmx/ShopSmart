package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ShopRepository @Inject constructor(private val api: ShopApi) {

    suspend fun addShop(
        name: RequestBody,
        address: RequestBody,
        category: RequestBody,
        description: RequestBody,
        shopType: RequestBody,
        position: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        imagesJson: RequestBody,
        imageParts: List<MultipartBody.Part>,
        documentParts: List<MultipartBody.Part>
    ): Response<AddShopResponse> {
        return api.addShop(
            name, address, category, description, shopType, position,
            latitude, longitude, imagesJson, imageParts, documentParts
        )
    }

//    suspend fun getShops(): Result<List<ShopItem>> {
//        return try {
//            val response = api.getShops()
//            if (response.isSuccessful) {
//                Result.success(response.body() ?: emptyList())
//            } else {
//                Result.failure(Exception(response.message()))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}

