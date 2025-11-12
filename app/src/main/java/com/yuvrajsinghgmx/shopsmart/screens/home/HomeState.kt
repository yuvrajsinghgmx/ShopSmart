package com.yuvrajsinghgmx.shopsmart.screens.home

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CategorizedProductsUi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchResult
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val shops: List<Shop> = emptyList(),
    val nearbyShops: List<Shop> = emptyList(),
    val categorizedProducts: List<CategorizedProductsUi> = emptyList(),
    val searchQuery: String? = null,
    val searchResults: List<SearchResult> = emptyList()
)