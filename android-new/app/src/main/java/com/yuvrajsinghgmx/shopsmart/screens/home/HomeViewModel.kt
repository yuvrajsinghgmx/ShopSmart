package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchResult
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    // Holds the result for UI: a pair of Product and corresponding Shop
    private val _state = mutableStateOf(
        HomeState(
            isLoading = false,
            products = emptyList(),
            allProducts = emptyList(),
            shops = emptyList(),
            nearbyShops = emptyList(),
            searchQuery = null,
            searchResults = emptyList(),
            error = null
        )
    )
    val state: State<HomeState> = _state


    // Local caches (used for filtering, so we never lose the originals)
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
                    searchQuery = ""
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
            is HomeEvent.LoadProducts, is HomeEvent.LoadShops -> {/* These load once in init, so ignore here. */
            }

            is HomeEvent.Search -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                filterResults(event.query)
            }
        }
    }

    /**
     * Filters products and shops for the search screen.
     * - Products matching name/category are paired to their shop.
     * - If shop name/category matches and not already included (no matching product), show the shop with product=null.
     * - Handles edge cases gracefully.
     */
    private fun filterResults(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            // If blank, show all products and their shops.
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
        // Find all products matching query
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
        // Optionally: include shop rows for shops matching query (but with no matching product above)
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