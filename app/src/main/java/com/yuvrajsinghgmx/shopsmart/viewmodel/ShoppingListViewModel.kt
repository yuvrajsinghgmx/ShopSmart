package com.yuvrajsinghgmx.shopsmart.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.datastore.ShoppingList
import com.yuvrajsinghgmx.shopsmart.datastore.dataStore
import com.yuvrajsinghgmx.shopsmart.datastore.getItems
import com.yuvrajsinghgmx.shopsmart.screens.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ShoppingListViewModel: ViewModel() {
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    fun updateItems(newItems: List<Product>) {
        _items.value = newItems
    }

    fun loadItems(context: Context) {
        viewModelScope.launch {
            getItems(context).collect { loadedItems ->
                _items.value = loadedItems
            }
        }
    }
}

fun getItems(context: Context): Flow<List<Product>> = context.dataStore.data
    .map { preferences ->
        val json = preferences[ShoppingList.ITEMS_KEY] ?: return@map emptyList()
        Gson().fromJson(json, Array<Product>::class.java).toList()
    }