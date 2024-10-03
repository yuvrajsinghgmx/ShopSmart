package com.yuvrajsinghgmx.shopsmart.Repository

import com.yuvrajsinghgmx.shopsmart.api.API
import javax.inject.Inject

class ImageRepo @Inject constructor(private val api: API) {

    suspend fun getProducts(SearchQuery: String): List<String>? {
        return try {
            val response = api.getProducts(SearchQuery)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.hits?.map { it.largeImageURL }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}