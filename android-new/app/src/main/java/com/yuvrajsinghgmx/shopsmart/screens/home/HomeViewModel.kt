package com.yuvrajsinghgmx.shopsmart.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CategorizedProductsUi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchResult
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.HomeRepository
import com.yuvrajsinghgmx.shopsmart.screens.auth.LoginScreen
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.UiEvent
import com.yuvrajsinghgmx.shopsmart.utils.toUiProduct
import com.yuvrajsinghgmx.shopsmart.utils.toUiShop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        HomeState(
            isLoading = false,
            products = emptyList(),
            allProducts = emptyList(),
            shops = emptyList(),
            nearbyShops = emptyList(),
            categorizedProducts = emptyList(),
            searchQuery = null,
            searchResults = emptyList(),
            error = null
        )
    )
    val state: State<HomeState> = _state

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var allProducts: List<Product> = emptyList()
    private var allShops: List<Shop> = emptyList()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = homeRepository.getLoadData() // Retrofit call

                // Trending products
                val trendingProductsDeferred = async(Dispatchers.Default) {
                    data.trendingProducts.map { it.toUiProduct()
                    }
                }

                // Categorized products
                val categorizedDeferred = async(Dispatchers.Default){
                    data.categorizedProducts.map { category ->
                        CategorizedProductsUi(
                            type = category.type,
                            items = category.items.map { it.toUiProduct() }
                        )
                    }
                }

                // Nearby shops
                val nearbyShopsDeferred = async(Dispatchers.Default){
                    data.nearbyShops.flatMap { shopGroup ->
                        shopGroup.items.map { it.toUiShop() }
                    }
                }

                val trendingProducts = trendingProductsDeferred.await()
                val categorized = categorizedDeferred.await()
                val nearbyShops = nearbyShopsDeferred.await()

                // Cache for search
                allProducts = trendingProducts + categorized.flatMap { it.items }
                allShops = nearbyShops

                val newState = HomeState(
                    isLoading = false,
                    products = trendingProducts,
                    allProducts = allProducts,
                    shops = nearbyShops,
                    nearbyShops = nearbyShops,
                    categorizedProducts = categorized,
                    searchResults = allProducts.map { product ->
                        SearchResult(
                            product = product,
                            shop = allShops.find { it.name == product.shopName }
                        )
                    },
                    searchQuery = ""
                )
                // Update UI on Main thread
                withContext(Dispatchers.Main) {
                    _state.value = newState
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load data"
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

    private fun filterResults(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            // If blank, show all products and their shops.
            _state.value = _state.value.copy(
                searchResults = allProducts.map { product ->
                    SearchResult(
                        product = product,
                        shop = allShops.find { it.name == product.shopName }
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
                    allShops.find { it.name == product.shopName }
                if (shop != null) matchedShops.add(shop.name)
                results.add(SearchResult(product = product, shop = shop))
            }
        }
        // Optionally: include shop rows for shops matching query (but with no matching product above)
        allShops.forEach { shop ->
            if (!matchedShops.contains(shop.name) &&
                (shop.name.contains(lowerQ, ignoreCase = true) ||
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