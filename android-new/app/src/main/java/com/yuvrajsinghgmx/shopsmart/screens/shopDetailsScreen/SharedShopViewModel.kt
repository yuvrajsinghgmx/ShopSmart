package com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.ShopDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedShopViewModel @Inject constructor(
    private  val shopDetailsRepository: ShopDetailsRepository
) : ViewModel() {
    private val _selectedShop = MutableStateFlow<Shop?>(null)
    val selectedShop: StateFlow<Shop?> = _selectedShop

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _shopDetails = MutableStateFlow<ShopResponse?>(null)
    val shopDetails: MutableStateFlow<ShopResponse?> = _shopDetails

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
            }.onFailure { e ->
                _error.value = e.message ?: "Failed to load product"
            }
            _loading.value = false
        }
    }

    fun clearSelectedShop() {
        _selectedShop.value = null
    }
}