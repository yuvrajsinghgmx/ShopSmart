package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.datastore.getItems
import com.yuvrajsinghgmx.shopsmart.repository.ImageRepo
import com.yuvrajsinghgmx.shopsmart.screens.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val imageRepo: ImageRepo,
    @ApplicationContext context: Context
) : ViewModel() {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    init {
        loadItems(context)
    }

    fun updateItems(newItems: List<Product>) {
        _items.value = newItems
    }

    fun loadItems(context: Context) {
        viewModelScope.launch {
            val products = getItems(context).first() // Get the initial list of products
            val updatedProducts = products.map { product ->
                val imageUrl = searchImage(product.name)
                product.copy(imageUrl = imageUrl) // Update the imageUrl for each product
            }
            _items.value = updatedProducts // Update the state with the updated products
        }
    }

    suspend fun searchImage(query: String): String? {
        val galleryUrls = imageRepo.getProducts(query)
        return galleryUrls?.firstOrNull() // Return the first gallery URL if available
    }

    fun search(keyWord: String){
        _items.value = _items.value.sortedWith(compareBy<Product> {
            it.name.startsWith(keyWord, ignoreCase = true)
        }.thenBy {
            it.name.contains(keyWord, ignoreCase = true)
        }.thenBy {
            it.name
        }).reversed()
    }
}