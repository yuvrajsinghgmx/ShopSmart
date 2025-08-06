package com.yuvrajsinghgmx.shopsmart.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.modelclass.Category

data class Product(
    val id: Int,
    val name: String,
    val category: Category
)

class ProductViewModel : ViewModel() {
    private val allProducts = listOf(
        Product(1, "Milk", Category.Groceries),
        Product(2, "Jeans", Category.Fashion),
        Product(3, "TV", Category.Electronics),
        Product(4, "Bread", Category.Groceries),
        Product(5, "T-shirt", Category.Fashion),
    )

    private val _selectedCategory = mutableStateOf(Category.All)
    val selectedCategory: State<Category> = _selectedCategory

    val filteredProducts: State<List<Product>> = derivedStateOf {
        if (_selectedCategory.value == Category.All) allProducts
        else allProducts.filter { it.category == _selectedCategory.value }
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }
}