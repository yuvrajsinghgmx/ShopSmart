package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _isProductSaved = MutableStateFlow(false)
    val isProductSaved: StateFlow<Boolean> = _isProductSaved

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    fun onSaveClick(productId: String) {
        viewModelScope.launch {
            val result = repository.toggleFavoriteProduct(productId)
            result.onSuccess { response ->
                _isProductSaved.value = response.isFavorite
                val message = if (response.isFavorite) {
                    "Product saved to favorite"
                } else {
                    "Product removed from favorite"
                }
                _eventFlow.emit((UiEvent.ShowToast(message)))
            }.onFailure { e ->
                _error.value = e.message ?: "toggle Failed"
            }
        }
    }

    fun setInitialFavoriteState(isFavorite: Boolean) {
        _isProductSaved.value = isFavorite
    }
}