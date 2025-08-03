package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {

    val isProductSaved = mutableStateOf(false)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    fun onShareClick(product: Product) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShareProduct(product))
        }
    }

    fun onCallClick(number: String) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.CallShop(number))
        }
    }

    fun onSaveClick(product: Product) {
        isProductSaved.value = true
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast("${product.name} saved!"))
        }
    }
}