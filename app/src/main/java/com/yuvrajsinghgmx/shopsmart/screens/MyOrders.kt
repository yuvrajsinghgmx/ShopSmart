package com.yuvrajsinghgmx.shopsmart.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.utils.SharedPrefsHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrders(navController: NavController) {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(listOf<Product>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        loadOrders(context) { loadedOrders ->
            orders = loadedOrders
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                clearOrders(context)
                                orders = emptyList()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Orders"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (orders.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total:",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "₹${orders.sumOf { it.amount }}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* No functionality added */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                        ) {
                            Text("Proceed to Payment", fontSize = 18.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No orders yet. Start shopping!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp)
            ) {
                items(orders) { order ->
                    OrderItem(order)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = order.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Quantity: 1", // Assuming quantity is 1, adjust if needed
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "₹${order.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private suspend fun loadOrders(context: android.content.Context, onOrdersLoaded: (List<Product>) -> Unit) {
    try {
        val loadedOrders = SharedPrefsHelper.getOrders(context)
        Log.d("MyOrders", "Orders loaded: ${loadedOrders.size}")
        Toast.makeText(context, "Orders loaded: ${loadedOrders.size}", Toast.LENGTH_SHORT).show()
        onOrdersLoaded(loadedOrders)
    } catch (e: Exception) {
        Log.e("MyOrders", "Error loading orders", e)
        Toast.makeText(context, "Error loading orders", Toast.LENGTH_SHORT).show()
        onOrdersLoaded(emptyList())
    }
}

private suspend fun clearOrders(context: android.content.Context) {
    try {
        SharedPrefsHelper.saveOrders(context, emptyList())
        Log.d("MyOrders", "Orders cleared")
        Toast.makeText(context, "All orders cleared", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("MyOrders", "Error clearing orders", e)
        Toast.makeText(context, "Error clearing orders", Toast.LENGTH_SHORT).show()
    }
}