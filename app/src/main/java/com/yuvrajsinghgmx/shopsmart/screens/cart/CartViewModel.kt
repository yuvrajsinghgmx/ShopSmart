package com.yuvrajsinghgmx.shopsmart.screens.cart

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CartResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CreateOrderResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _cart = MutableStateFlow<CartResponse?>(null)
    val cart: StateFlow<CartResponse?> = _cart

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _orderCreated = MutableStateFlow<CreateOrderResponse?>(null)
    val orderCreated: StateFlow<CreateOrderResponse?> = _orderCreated

    fun getCart() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cart.value = repository.getCart()
            } catch (e: Exception) {
                _cart.value = null
                Log.e("CartViewModel", "Error adding to cart: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun addToCart(context: Context, productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cart.value = repository.addToCart(productId, quantity)
                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error adding to cart: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun createOrder(shopId: Int, address: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _orderCreated.value = repository.createOrder(shopId, address)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error adding to cart: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

//    fun clearCart(shopId: Int) {
//        viewModelScope.launch {
//            _loading.value = true
//            try {
//                repository.clearCart(shopId)
//                _cart.value = CartResponse(0, 0, emptyList(), 0.0)
//            } catch (e: Exception) {
//                Log.e("CartViewModel", "Error clearing cart: ${e.message}")
//            } finally {
//                _loading.value = false
//            }
//        }
//    }

    fun removeFromCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cart.value = repository.removeFromCart(productId, quantity)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error removing from cart: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}