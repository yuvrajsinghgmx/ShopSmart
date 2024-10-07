package com.yuvrajsinghgmx.shopsmart.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


data class Product(
    val name: String,
    val amount: Int,
    val imageUrl: String? = null,
    val isChecked: Boolean = false // Ensure this property exists
)

object ShoppingList {
    val ITEMS_KEY = stringPreferencesKey("items")
}

suspend fun saveItems(context: Context, items: List<Product>) { // Changed Poduct to Product
    context.dataStore.edit { preferences ->
        val jsonString = Gson().toJson(items)
        preferences[ShoppingList.ITEMS_KEY] = jsonString
    }
}

fun getItems(context: Context): Flow<List<Product>> = context.dataStore.data
    .map { preferences ->
        val json = preferences[ShoppingList.ITEMS_KEY] ?: return@map emptyList()
        Gson().fromJson(json, Array<Product>::class.java).toList() // Ensure this matches the Product class
    }