package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedShopViewModel : ViewModel() {
    private val _selectedShop = MutableStateFlow<Shop?>(null)
    val selectedShop: StateFlow<Shop?> = _selectedShop

    fun setSelectedShop(shop: Shop) {
        _selectedShop.value = shop
    }
}