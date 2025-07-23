package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.datastore.Product
import com.yuvrajsinghgmx.shopsmart.datastore.getItems
import com.yuvrajsinghgmx.shopsmart.repository.ImageRepo
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
//    private val _items = MutableStateFlow<List<Product>>(emptyList())
//    val items: StateFlow<List<Product>> = _items.asStateFlow()
    private val _items = mutableStateListOf<Product>()
    val items: SnapshotStateList<Product> = _items


    init {
        loadItems(context)
    }

    /*fun updateItems(newItems: List<Product>) {
        _items.value = newItems
    }*/

    fun updateItems(newItems: List<Product>) {
        viewModelScope.launch {
            _items.clear()
            _items.addAll(newItems)
        }
    }

    fun loadItems(context: Context) {
        viewModelScope.launch {
            val products = getItems(context).first() // Get the initial list of products
            val updatedProducts = products.map { product ->
                val imageUrl = searchImage(product.name)
                product.copy(id = product.id, imageUrl = imageUrl) // Update the imageUrl for each product
            }
            updateItems(updatedProducts) // Update the state with the updated products
        }
    }

    suspend fun searchImage(query: String): String? {
        val galleryUrls = imageRepo.getProducts(query)
        return galleryUrls?.firstOrNull() // Return the first gallery URL if available
    }

    /*fun search(keyWord: String){
        _items.value = _items.value.sortedWith(compareBy<Product> {
            it.name.startsWith(keyWord, ignoreCase = true)
        }.thenBy {
            it.name.contains(keyWord, ignoreCase = true)
        }.thenBy {
            it.name
        }).reversed()
    }*/

    fun search(keyword: String) {
        // Create a sorted list based on the search criteria
        val sortedList = _items.sortedWith(
            compareBy<Product> {
                it.name.startsWith(keyword, ignoreCase = true)
            }.thenBy {
                it.name.contains(keyword, ignoreCase = true)
            }.thenBy {
                it.name
            }
        ).reversed()

        // Clear the current items in _items and add the sorted items
        _items.clear()
        _items.addAll(sortedList)
    }
}