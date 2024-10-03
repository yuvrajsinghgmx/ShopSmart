package com.yuvrajsinghgmx.shopsmart.api

import com.yuvrajsinghgmx.shopsmart.ApiData.Pics
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface API {
    @GET("/api/?key=42550834-3da29e9037035d6f812732b15")
    suspend fun getProducts(@Query("q") SearchQuery: String): Response<Pics>

}