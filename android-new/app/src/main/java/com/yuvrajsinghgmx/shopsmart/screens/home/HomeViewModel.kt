package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    init {
        onEvent(HomeEvent.LoadProducts)
        onEvent(HomeEvent.LoadShops)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadProducts -> {
                homeProductData()
            }

            is HomeEvent.LoadShops -> {
                homeShopData()
            }

            is HomeEvent.Search -> {
                _state.value = _state.value.copy(
                    searchQuery = event.query
                )
                filterProducts(searchQuery = event.query)
            }
        }
    }

    private fun filterProducts(searchQuery: String) {
        val allProducts = _state.value.allProducts
        val filtered = if (searchQuery.isBlank()) {
            allProducts
        } else {
            allProducts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true)
            }
        }
        _state.value = _state.value.copy(products = filtered)
    }

    private fun homeProductData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            try {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        products = repository.getProductList(),
                        allProducts = repository.getProductList(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unexpected error occurred"
                )
            }
        }
    }

    private fun homeShopData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            try {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        nearbyShops = repository.getNearbyShops(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unexpected error occurred"
                )
            }
        }
    }
}