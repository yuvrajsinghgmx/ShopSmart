package com.yuvrajsinghgmx.shopsmart.repository

import android.util.Log
import com.yuvrajsinghgmx.shopsmart.api.API
import javax.inject.Inject

class ImageRepo @Inject constructor(private val api: API) {

    suspend fun getProducts(searchQuery: String): List<String> {
        return try {
            val response = api.getProducts(searchQuery)
            if (response.isSuccessful) {
                response.body()?.hits?.map { it.largeImageURL }.orEmpty()
            } else {
                Log.e("ImageRepo", "Error: ${response.code()} - ${response.message()}")
                emptyList() // Return empty list if response isn't successful
            }
        } catch (e: Exception) {
            Log.e("ImageRepo", "Exception: ${e.message}", e) // Log exception
            emptyList() // Return empty list in case of an exception
        }
    }
}