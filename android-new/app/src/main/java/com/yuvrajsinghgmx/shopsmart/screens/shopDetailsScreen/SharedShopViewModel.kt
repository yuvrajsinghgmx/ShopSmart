package com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.CartRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.ShopDetailsRepository
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedShopViewModel @Inject constructor(
    private  val shopDetailsRepository: ShopDetailsRepository,
    private val favRepository: FavoritesRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _selectedShop = MutableStateFlow<Shop?>(null)
    val selectedShop: StateFlow<Shop?> = _selectedShop

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _shopDetails = MutableStateFlow<ShopResponse?>(null)
    val shopDetails: MutableStateFlow<ShopResponse?> = _shopDetails

    private val _isShopSaved = MutableStateFlow(false)
    val isShopSaved: StateFlow<Boolean> = _isShopSaved

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    fun setSelectedShop(shop: Shop) {
        _selectedShop.value = shop
    }

    fun getShopDetails(shopId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = shopDetailsRepository.getShopDetails(shopId)
            result.onSuccess { response ->
                _shopDetails.value = response
                _isShopSaved.value = response.isFavorite
            }.onFailure { e ->
                _error.value = e.message ?: "Failed to load product"
            }
            _loading.value = false
        }
    }

    fun toggleFavoriteShop(id: Int) {
        viewModelScope.launch {
            val result = favRepository.toggleFavoriteShop(id)
            result.onSuccess { response ->
                _isShopSaved.value = response.isFavorite
                val message = if (response.isFavorite) {
                    "Shop added to favorite"
                } else {
                    "Shop removed from favorite"
                }
                _eventFlow.emit(UiEvent.ShowToast(message))
            }.onFailure { e ->
                Log.d("Shop Toggle Error", "toggleFavoriteShop: ${e.message}")
                _error.value = e.message ?: "toggle Failed"
                _eventFlow.emit(UiEvent.ShowToast("toggle Failed"))
            }

        }
    }

    fun initialStateFavorite(isFavorite: Boolean) {
        _isShopSaved.value = isFavorite
    }

    fun addToCart(productId: String){
        viewModelScope.launch {
            cartRepository.addToCart(productId)
        }
    }
}