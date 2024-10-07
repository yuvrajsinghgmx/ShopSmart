package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.Repository.ImageRepo
import com.yuvrajsinghgmx.shopsmart.datastore.getItems
import com.yuvrajsinghgmx.shopsmart.datastore.Product // Ensure you import the correct Product class
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val imageRepo: ImageRepo
) : ViewModel() {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    fun updateItems(newItems: List<Product>) {
        _items.value = newItems
    }

    fun loadItems(context: Context) {
        viewModelScope.launch {
            val products = getItems(context).first()
            val updatedProducts = products.map { product ->
                // Highlighted line
                val imageUrl = searchImage(product.name)
                // Ensure you're calling the copy on the correct Product data class with isChecked
                product.copy(imageUrl = imageUrl, isChecked = false)
            }
            _items.value = updatedProducts
        }
    }
    suspend fun searchImage(query: String): String? {
        val galleryUrls = imageRepo.getProducts(query)
        return galleryUrls?.firstOrNull()
    }
    fun areAllItemsChecked(): Boolean {
        return _items.value.all { it.isChecked }
    }

    // Check if any item is still unchecked
    fun isAnyItemUnchecked(): Boolean {
        return _items.value.any { !it.isChecked }
    }
}