package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedShopResponse
import retrofit2.http.GET

interface FavoritesApi {
    @GET("api/favorites/products/")
    suspend fun getSavedProducts(): List<SavedProductResponse>

    @GET("api/favorites/shops/")
    suspend fun getSavedShops(): List<SavedShopResponse>
}