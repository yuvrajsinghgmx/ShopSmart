package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products/search/")
    suspend fun searchProducts(@Query("query") query: String): Response<List<SearchProductResponse>>

}