package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.modelclass.SearchResult
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _state = mutableStateOf(
        HomeState(
            isLoading = false,
            products = emptyList(),
            allProducts = emptyList(),
            shops = emptyList(),
            nearbyShops = emptyList(),
            searchQuery = null,
            searchResults = emptyList(),
            error = null,
            selectedCategory = "All"
        )
    )
    val state: State<HomeState> = _state

    private var allProducts: List<Product> = emptyList()
    private var allShops: List<Shop> = emptyList()

    init {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                allProducts = repository.getProductList()
                allShops = repository.getNearbyShops()
                _state.value = _state.value.copy(
                    isLoading = false,
                    products = allProducts,
                    allProducts = allProducts,
                    shops = allShops,
                    nearbyShops = allShops,
                    searchResults = allProducts.map { product ->
                        SearchResult(
                            product = product,
                            shop = allShops.find { it.shopName == product.shopName || it.shopNumber.toString() == product.shopNumber }
                        )
                    },
                    searchQuery = "",
                    selectedCategory = "All"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load data."
                )
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadProducts, is HomeEvent.LoadShops -> {
            }
            is HomeEvent.Search -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                filterResults(event.query)
            }
            is HomeEvent.SelectCategory -> {
                _state.value = _state.value.copy(selectedCategory = event.category)
                filterByCategory(event.category)
            }
        }
    }

    private fun filterByCategory(category: String) {
        val trimmed = category.trim()
        val filtered = if (trimmed.equals("All", ignoreCase = true)) {
            allProducts
        } else {
            allProducts.filter { it.category.equals(trimmed, ignoreCase = true) }
        }
        _state.value = _state.value.copy(products = filtered)
    }

    private fun filterResults(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            _state.value = _state.value.copy(
                searchResults = allProducts.map { product ->
                    SearchResult(
                        product = product,
                        shop = allShops.find { it.shopName == product.shopName || it.shopNumber.toString() == product.shopNumber }
                    )
                },
                error = null
            )
            return
        }
        val results = mutableListOf<SearchResult>()
        val lowerQ = trimmed.lowercase()
        val matchedShops = mutableSetOf<String>()
        allProducts.forEach { product ->
            if (
                product.name.contains(lowerQ, ignoreCase = true) ||
                product.category.contains(lowerQ, ignoreCase = true)
            ) {
                val shop =
                    allShops.find { it.shopName == product.shopName || it.shopNumber.toString() == product.shopNumber }
                if (shop != null) matchedShops.add(shop.shopName)
                results.add(SearchResult(product = product, shop = shop))
            }
        }
        allShops.forEach { shop ->
            if (!matchedShops.contains(shop.shopName) &&
                (shop.shopName.contains(lowerQ, ignoreCase = true) ||
                        shop.category.contains(lowerQ, ignoreCase = true))
            ) {
                results.add(SearchResult(product = null, shop = shop))
            }
        }
        _state.value = _state.value.copy(
            searchResults = results,
            error = if (results.isEmpty()) "No results found for '$query'" else null
        )
    }
}
