package com.yuvrajsinghgmx.shopsmart.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate loading cart items
            val sampleItems = getSampleCartItems()

            _uiState.value = _uiState.value.copy(
                cartItems = sampleItems,
                isLoading = false,
                totalAmount = calculateTotalAmount(sampleItems),
                selectedItemsCount = sampleItems.count { it.isSelected }
            )
        }
    }

    private fun updateStateAndRecalculate(updatedItems: List<CartItem>) {
        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            // Crucial synchronization lines: Recalculate totals and counts
            totalAmount = calculateTotalAmount(updatedItems),
            selectedItemsCount = updatedItems.count { it.isSelected }
        )
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        //val currentItems = _uiState.value.cartItems
        val updatedItems = _uiState.value.cartItems.map { item ->
            if (item.product.productId == productId) {
                item.copy(quantity = maxOf(1, newQuantity))
            } else {
                item
            }
        }
        updateStateAndRecalculate(updatedItems)
/*        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            totalAmount = calculateTotalAmount(updatedItems)
        )*/
    }

    fun toggleItemSelection(productId: String) {
        val updatedItems = _uiState.value.cartItems.map { item ->
            if (item.product.productId == productId) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        updateStateAndRecalculate(updatedItems)
    }

    fun removeItem(productId: String) {
        val updatedItems = _uiState.value.cartItems.filter { it.product.productId != productId }
        updateStateAndRecalculate(updatedItems)
    }

    fun clearCart() {
        _uiState.value = _uiState.value.copy(
            cartItems = emptyList(),
            totalAmount = 0.0,
            selectedItemsCount = 0
        )
    }

/*    private fun calculateTotalAmount(items: List<CartItem>): Double {
        return items.filter { it.isSelected }
            .sumOf { (it.product.price.toDoubleOrNull() ?: 0.0) * it.quantity.toDouble() }
    }*/

    private fun calculateTotalAmount(items: List<CartItem>): Double {
        return items.filter { it.isSelected }
            .sumOf {
                val numericPrice = it.product.price
                    .replace(Regex("[^\\d.]"), "") // removes $ or any non-numeric chars
                    .toDoubleOrNull() ?: 0.0

                numericPrice * it.quantity
            }
    }


    private fun getSampleCartItems(): List<CartItem> {
        return listOf(
            CartItem(
                product = Product(
                    productId = "1",
                    name = "Premium Leather Tote Bag",
                    price = "$89.99",
                    category = "Bags",
                    review = "4.8",
                    description = "Elegant leather tote bag for all occasions.",
                    imageUrl = listOf("https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80"),
                    shopName = "Urban Leather Co.",
                    shopNumber = "101",
                    distance = "2.1 km",
                    shopId = "1",
                    isFavorite = true
                ),
                quantity = 1
            ),
            CartItem(
                product = Product(
                    productId = "2",
                    name = "Classic Leather Backpack",
                    price = "$129.99",
                    category = "Bags",
                    review = "4.6",
                    description = "Spacious, stylish backpack in genuine leather.",
                    imageUrl = listOf("https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80"),
                    shopId = "2",
                    isFavorite = true,
                    shopName = "Leather Workshop",
                    shopNumber = "102",
                    distance = "3.4 km"
                ),
                quantity = 1
            ),
            CartItem(
                product = Product(
                    productId = "3",
                    name = "Handcrafted Leather Wallet",
                    price = "$49.99",
                    category = "Accessories",
                    review = "4.7",
                    description = "Durable and classic handcrafted wallet.",
                    imageUrl = listOf("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80"),
                    shopId = "3",
                    isFavorite = false,
                    shopName = "Artisan Leather",
                    shopNumber = "103",
                    distance = "4.2 km"
                ),
                quantity = 2,
                isSelected = false
            ),
            CartItem(
                product = Product(
                    productId = "4",
                    name = "Handcrafted Leather Wallet",
                    price = "$49.99",
                    category = "Accessories",
                    review = "4.7",
                    description = "Durable and classic handcrafted wallet.",
                    imageUrl = listOf("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80"),
                    shopId = "3",
                    isFavorite = false,
                    shopName = "Artisan Leather",
                    shopNumber = "103",
                    distance = "4.2 km"
                ),
                quantity = 2,
                isSelected = false
            )
        )
    }
}