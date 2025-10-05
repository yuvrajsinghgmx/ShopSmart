package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
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
        imageParts: List<MultipartBody.Part>,
        documentParts: List<MultipartBody.Part>
    ): Response<ShopResponse> {
        return api.addShop(
            name, address, category, description, shopType, position,
            latitude, longitude,imageParts, documentParts
        )
    }
}

