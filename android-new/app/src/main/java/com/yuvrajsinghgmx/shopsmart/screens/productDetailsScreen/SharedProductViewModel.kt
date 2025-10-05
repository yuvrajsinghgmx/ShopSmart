package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ProductDetailResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // Full product details directly from API
    private val _productDetails = MutableStateFlow<ProductDetailResponse?>(null)
    val productDetails: StateFlow<ProductDetailResponse?> = _productDetails

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // fetch product details by ID
    fun fetchProductDetails(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = productRepository.getProductDetails(id)
            result.onSuccess { response ->
                _productDetails.value = response
            }.onFailure { e ->
                _error.value = e.message ?: "Failed to load product"
            }
            _loading.value = false
        }
    }

    // toggle favorite
    fun onSaveClick() {
        val productId = _productDetails.value?.productId ?: return
        viewModelScope.launch {
            val result = favoritesRepository.toggleFavoriteProduct(productId)
            result.onSuccess { response ->
                _productDetails.value = _productDetails.value?.copy(isFavorite = response.isFavorite)
                val message = if (response.isFavorite) "Product saved to favorite"
                else "Product removed from favorite"
                _eventFlow.emit(UiEvent.ShowToast(message))
            }.onFailure { e ->
                _error.value = e.message ?: "Failed to toggle favorite"
            }
        }
    }

    // share product
    fun onShareClick() {
        _productDetails.value?.let {
            viewModelScope.launch { _eventFlow.emit(UiEvent.ShareProduct(it)) }
        }
    }

    // call shop
    fun onCallClick() {
        val phoneNumber = _productDetails.value?.shopDetails?.get("phone") ?: return
        viewModelScope.launch { _eventFlow.emit(UiEvent.CallShop(phoneNumber)) }
    }
}