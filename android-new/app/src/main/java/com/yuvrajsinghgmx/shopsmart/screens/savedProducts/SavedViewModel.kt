package com.yuvrajsinghgmx.shopsmart.screens.savedProducts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedShopResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val repository: FavoritesRepository) :
    ViewModel() {

    private val _savedProducts = MutableStateFlow<List<SavedProductResponse>>(emptyList())
    val savedProducts: StateFlow<List<SavedProductResponse>> = _savedProducts

    private val _savedShops = MutableStateFlow<List<SavedShopResponse>>(emptyList())
    val savedShops: StateFlow<List<SavedShopResponse>> = _savedShops

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage

    private val _isProductLoading = MutableStateFlow<Boolean>(false)
    val isProductLoading : StateFlow<Boolean> =_isProductLoading

    private val _isShopLoading = MutableStateFlow<Boolean>(false)
    val isShopLoading : StateFlow<Boolean> =_isShopLoading

    init {
        fetchSavedProducts()
        fetchSavedShops()
    }

    private fun fetchSavedProducts() {
        viewModelScope.launch {
            _isProductLoading.value = true
            try {
                val response = repository.getSavedProducts()
                _savedProducts.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "something went wrong while fetching saved products"
            }finally {
                _isProductLoading.value = false
            }
        }
    }

    private fun fetchSavedShops() {
        viewModelScope.launch {
            _isShopLoading.value = true
            try {
                val response = repository.getSavedShops()
                _savedShops.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "something went wrong while fetching saved shops"
            }finally {
                _isShopLoading.value = false
            }
        }
    }

    fun onFavoriteProductIconClick(id: Int) {
        viewModelScope.launch {
            try{
                val result = repository.toggleFavoriteProduct(id)
                result.onSuccess { response ->
                    _savedProducts.value = _savedProducts.value.map { product ->
                        if (product.id == id) {
                            product.copy(isFavorite = response.isFavorite)
                        } else {
                            product
                        }
                    }
                    if(!response.isFavorite){
                        _snackbarMessage.emit("Removed from favorites")
                    }
                    fetchSavedProducts()
                }.onFailure {
                    _error.value = it.message ?: "Failed"
                    _snackbarMessage.emit("Failed to remove")
                }
            }catch (e: Exception){
                Log.e("Saved Screen :","Toggle failed in saved Screen")
            }

        }
    }

    fun onFavoriteShopIconClick(id:Int){
        viewModelScope.launch {
            try{
                val result = repository.toggleFavoriteShop(id)
                result.onSuccess { response ->
                    _savedShops.value = _savedShops.value.map { product ->
                        if(product.id == id){
                            product.copy(isFavorite = response.isFavorite)
                        }else{
                            product
                        }
                    }
                    if(!response.isFavorite){
                        _snackbarMessage.emit("Removed from favorites")
                    }
                    fetchSavedShops()
                }.onFailure {
                    _error.value = it.message?:"Failed"
                    _snackbarMessage.emit("Failed to remove")
                }
            }catch (e: Exception){
                Log.e("Saved Screen :","Toggle failed in saved Screen")
            }

        }
    }

}