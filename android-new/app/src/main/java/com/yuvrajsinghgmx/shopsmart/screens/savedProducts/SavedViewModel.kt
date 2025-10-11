package com.yuvrajsinghgmx.shopsmart.screens.savedProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedProductResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedShopResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val repository: FavoritesRepository) : ViewModel(){

    private val _savedProducts = MutableStateFlow<List<SavedProductResponse>>(emptyList())
    val savedProducts: StateFlow<List<SavedProductResponse>> = _savedProducts

    private val _savedShops = MutableStateFlow<List<SavedShopResponse>>(emptyList())
    val savedShops: StateFlow<List<SavedShopResponse>> = _savedShops

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchSavedProducts()
        fetchSavedShops()
    }

    private fun fetchSavedProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getSavedProducts()
                withContext(Dispatchers.Main) {
                    _savedProducts.value = response
                }
            }catch (e: Exception){
                _error.value = e.message?:"something went wrong while fetching saved products"
            }
        }
    }
    private fun fetchSavedShops(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getSavedShops()
                withContext(Dispatchers.Main) {
                    _savedShops.value = response
                }
            }catch (e: Exception){
                _error.value = e.message?:"something went wrong while fetching saved shops"
            }
        }
    }
}