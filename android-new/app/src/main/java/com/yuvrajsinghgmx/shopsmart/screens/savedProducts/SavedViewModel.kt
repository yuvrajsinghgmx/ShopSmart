package com.yuvrajsinghgmx.shopsmart.screens.savedProducts

import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val repository: Repository) : ViewModel(){

    private val _savedProducts = MutableStateFlow<List<Product>>(emptyList())
    val savedProducts: StateFlow<List<Product>> = _savedProducts

    private val _savedShops = MutableStateFlow<List<Shop>>(emptyList())
    val savedShops: StateFlow<List<Shop>> = _savedShops

    init {
        val user = repository.getUserData()
        _savedProducts.value = user.savedProducts
        _savedShops.value = user.savedShops
    }

}