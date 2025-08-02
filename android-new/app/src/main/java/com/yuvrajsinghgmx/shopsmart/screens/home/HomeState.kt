package com.yuvrajsinghgmx.shopsmart.screens.home

import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.modelclass.SearchResult
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop

data class HomeState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val error: String? = null,
    val shops: List<Shop> = emptyList(),
    val nearbyShops: List<Shop> = emptyList(),
    val searchQuery: String? = null,
    val searchResults: List<SearchResult> = emptyList()
)
