package com.yuvrajsinghgmx.shopsmart.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Cart(viewModel: ShoppingListViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCost = cartItems.sumOf { it.amount }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Your Cart", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(cartItems) { item ->
                CartItemView(item) {
                    viewModel.removeItemFromCart(item) // Remove item from cart
                }
            }

            // If cart is empty
            if (cartItems.isEmpty()) {
                item {
                    Text(text = "Cart is empty", modifier = Modifier.padding(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Total: ₹${totalCost}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                // Navigate to checkout screen or perform checkout action
                val selectedItemsJson = Gson().toJson(cartItems)
                navController.navigate("MyOrders?selectedItems=$selectedItemsJson")

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = cartItems.isNotEmpty() // Disable button if cart is empty
        ) {
            Text(text = "Checkout")
        }
    }
}

@Composable
fun CartItemView(item: Product, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Price: ₹${item.amount}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}
