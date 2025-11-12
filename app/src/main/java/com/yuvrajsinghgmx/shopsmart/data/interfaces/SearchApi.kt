package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("products/search/")
    suspend fun searchProducts(@Query("q") query: String): List<SearchItem>
}