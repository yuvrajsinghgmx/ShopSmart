package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.LoadApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.HomeResponse
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: LoadApi
) {
    suspend fun getLoadData(): HomeResponse {
        val response = api.getLoadData()
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception(response.message() ?: "Failed to load data")
        }
    }
}
