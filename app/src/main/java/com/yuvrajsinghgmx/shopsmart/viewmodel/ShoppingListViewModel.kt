package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.Repository.ImageRepo
import com.yuvrajsinghgmx.shopsmart.datastore.ShoppingList
import com.yuvrajsinghgmx.shopsmart.datastore.dataStore
import com.yuvrajsinghgmx.shopsmart.datastore.getItems
import com.yuvrajsinghgmx.shopsmart.screens.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val imageRepo: ImageRepo
) : ViewModel() {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    // New StateFlow to hold today's list
    private val _todayItems = MutableStateFlow<List<Product>>(emptyList())
    val todayItems: StateFlow<List<Product>> = _todayItems.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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

            // Filter todays items
            val today = LocalDate.now()
            val todayList = products.filter { product ->
                product.date != null && LocalDate.parse(product.date, dateFormatter).isEqual(today)
            }
            _todayItems.value = todayList // Update today's list
        }
    }

    // Add a new item
    fun addItem(name: String, price: String, date: String) {
    val amount = price.toIntOrNull() ?: 0
    val newProduct = Product(name = name, amount = amount, futureDate = date)
    val updatedItems = _items.value.toMutableList().apply { add(newProduct) }
    _items.value = updatedItems
}

    // Remove an item
    fun removeItem(product: Product) {
        val updatedItems = _items.value.toMutableList().apply { remove(product) }
        _items.value = updatedItems
    }

    suspend fun searchImage(query: String): String? {
        val galleryUrls = imageRepo.getProducts(query)
        return galleryUrls?.firstOrNull() // Return the first gallery URL if available
    }
}
