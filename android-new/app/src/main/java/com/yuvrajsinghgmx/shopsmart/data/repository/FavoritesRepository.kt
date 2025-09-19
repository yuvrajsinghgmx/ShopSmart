package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.FavoritesApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleFavoriteProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ToggleFavoriteShopResponse
import jakarta.inject.Inject

class FavoritesRepository @Inject constructor(
    private val api: FavoritesApi
) {
    suspend fun getSavedProducts() = api.getSavedProducts()
    suspend fun getSavedShops() = api.getSavedShops()

    suspend fun toggleFavoriteProduct(productId: String): Result<ToggleFavoriteProductResponse> {
        return try {
            val response = api.toggleFavoriteProduct(productId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavoriteShop(shopId: String): Result<ToggleFavoriteShopResponse> {
        return try {
            val response = api.toggleFavoriteShop(shopId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}