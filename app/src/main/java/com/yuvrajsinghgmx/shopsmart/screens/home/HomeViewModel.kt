package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CategorizedProductsUi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchResult
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.data.repository.HomeRepository
import com.yuvrajsinghgmx.shopsmart.utils.toUiProduct
import com.yuvrajsinghgmx.shopsmart.utils.toUiShop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        HomeState(
            isLoading = true,
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
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var allProducts: List<Product> = emptyList()
    private var allShops: List<Shop> = emptyList()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val (trendingProducts, categorized, nearbyShops) = withContext(Dispatchers.IO) {
                    val data = homeRepository.getLoadData()

                    val trendingProductsDeferred = async(Dispatchers.Default) {
                        data.trendingProducts.map { it.toUiProduct() }
                    }
                    val categorizedDeferred = async(Dispatchers.Default) {
                        data.categorizedProducts.map { category ->
                            CategorizedProductsUi(
                                type = category.type,
                                items = category.items.map { it.toUiProduct() }
                            )
                        }
                    }
                    val nearbyShopsDeferred = async(Dispatchers.Default) {
                        data.nearbyShops.flatMap { shopGroup ->
                            shopGroup.items.map { it.toUiShop() }
                        }
                    }
                    Triple(trendingProductsDeferred.await(), categorizedDeferred.await(), nearbyShopsDeferred.await())
                }

                allProducts = trendingProducts + categorized.flatMap { it.items }
                allShops = nearbyShops

                _state.update { currentState ->
                    currentState.copy(
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
                        searchQuery = "",
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load data"
                    )
                }
            }
        }
    }
}