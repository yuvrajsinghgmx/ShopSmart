package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
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

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val imageRepo: ImageRepo
) : ViewModel() {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

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
    fun addItemToCart(selectedItems: List<Product>) {
        println("Before : ${selectedItems}")

        _cartItems.value = _cartItems.value + selectedItems
        println("Cart updated: ${_cartItems.value}")
        Log.d("CartLog", "Adding item to cart:")
    }
    fun removeItemFromCart(item: Product) {
        _cartItems.value = _cartItems.value - item
    }

    // Clear all items from the cart
    fun clearCart() {
        _cartItems.value = emptyList()
    }
    fun updateCartItems(newCartItems: List<Product>) {

        _cartItems.value = newCartItems
    }


}