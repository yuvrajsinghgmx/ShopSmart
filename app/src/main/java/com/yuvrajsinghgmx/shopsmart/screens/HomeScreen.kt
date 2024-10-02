package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R

data class Product(val name: String, val amount: Int)

@Composable
fun HomeScreen() {
    val items by remember { mutableStateOf(SnapshotStateList<Product>()) }
    var newItem by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    var totalAmount by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = "ShopSmart",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(20.dp),
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { product ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Text(
                            text = product.name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 18.sp,
                            ),
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Text(
                            text = product.amount.toString(),
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(fontSize = 18.sp)
                        )
                        IconButton(onClick = {
                            totalAmount -= product.amount // Update total when deleting
                            items.remove(product)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

            // Display total amount
            Text(
                text = "Total Amount: $totalAmount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newItem,
                    onValueChange = { newItem = it },
                    placeholder = { Text("Add new item") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                TextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    placeholder = { Text("Add amount") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                // adding amount when button clicked and only when item and amount is not empty
                Button(
                    onClick = {
                        if (newItem.isNotBlank() && newAmount.isNotBlank()) {
                            val amountValue = newAmount.toIntOrNull() ?: 0
                            items.add(Product(newItem, amountValue))
                            totalAmount += amountValue // Update total amount
                            newItem = ""
                            newAmount = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add Item")
                }
            }
        }
    }
}
