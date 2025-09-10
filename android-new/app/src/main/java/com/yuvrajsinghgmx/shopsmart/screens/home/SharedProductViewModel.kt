package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedProductViewModel: ViewModel() {
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct : StateFlow<Product?> = _selectedProduct

    fun selectedProduct(product: Product){
        _selectedProduct.value = product
    }
}