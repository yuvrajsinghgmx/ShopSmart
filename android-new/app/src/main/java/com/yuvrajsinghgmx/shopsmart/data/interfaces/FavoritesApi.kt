package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleFavoriteProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleFavoriteShopResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoritesApi {
    @GET("favorites/products/")
    suspend fun getSavedProducts(): List<SavedProductResponse>

    @GET("favorites/shops/")
    suspend fun getSavedShops(): List<SavedShopResponse>

    @POST("products/{product_id}/toggle-favorite/")
    suspend fun toggleFavoriteProduct(
        @Path("product_id") id: Int
    ): ToggleFavoriteProductResponse

    @POST("shops/{shop_id}/toggle-favorite/")
    suspend fun toggleFavoriteShop(
        @Path("shop_id") shopId: String
    ): ToggleFavoriteShopResponse
}