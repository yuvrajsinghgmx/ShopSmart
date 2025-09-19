package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopDetailsViewmodel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {
    private val _isShopSaved = MutableStateFlow(false)
    val isShopSaved: StateFlow<Boolean> = _isShopSaved

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    fun toggleFavoriteShop(shopId: String) {
        viewModelScope.launch {
            val result = repository.toggleFavoriteShop(shopId)
            result.onSuccess { response ->
                _isShopSaved.value = response.isFavorite
                val message = if (response.isFavorite) {
                    "Shop added to favorite"
                } else {
                    "Shop removed from favorite"
                }
                _eventFlow.emit(UiEvent.ShowToast(message))
            }.onFailure { e ->
                _error.value = e.message ?: "toggle Failed"
                _eventFlow.emit(UiEvent.ShowToast("toggle Failed"))
            }

        }
    }

    fun initialStateFavorite(isFavorite: Boolean) {
        _isShopSaved.value = isFavorite
    }
}