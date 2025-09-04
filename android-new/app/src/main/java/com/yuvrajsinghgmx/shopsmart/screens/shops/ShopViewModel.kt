package com.yuvrajsinghgmx.shopsmart.screens.shops

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem
import com.yuvrajsinghgmx.shopsmart.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor (private val repository: ShopRepository) : ViewModel() {

    private val _shopResponse = MutableStateFlow<AddShopResponse?>(null)
    val shopResponse: StateFlow<AddShopResponse?> = _shopResponse

    private val _state = mutableStateOf(ShopState())
    val state: State<ShopState> = _state

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchShops()
    }

    fun saveShop(
        name: RequestBody,
        category: RequestBody,
        address: RequestBody,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        shopType: RequestBody,
        imageUploads: List<MultipartBody.Part>,
        documentUploads: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.addShop(
                name, category, address, description,
                latitude, longitude, shopType,
                imageUploads, documentUploads
            )
            result.onSuccess {
                _shopResponse.value = it
            }.onFailure {
                _error.value = it.message
            }
            _loading.value = false
        }
    }

    fun fetchShops() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = repository.getShops()
            result.onSuccess { shops ->
                _state.value = state.value.copy(shops = shops, isLoading = false)
                // Log shop details
                shops.forEach { shop ->
                    Log.d("ShopViewModel", "Shop: ${shop.name}, Owner: ${shop.owner_name}, Address: ${shop.address}")
                }
            }.onFailure { error ->
                _state.value = state.value.copy(error = error.message, isLoading = false)
            }
        }
    }

    data class ShopState(
        val shops: List<ShopItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}