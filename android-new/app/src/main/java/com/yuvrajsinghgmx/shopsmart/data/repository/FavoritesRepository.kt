package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.interfaces.FavoritesApi
import jakarta.inject.Inject

class FavoritesRepository@Inject constructor(
    private val api: FavoritesApi
) {
    suspend fun getSavedProducts() = api.getSavedProducts()
    suspend fun getSavedShops() = api.getSavedShops()
}