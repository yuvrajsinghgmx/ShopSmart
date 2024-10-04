package com.yuvrajsinghgmx.shopsmart.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.yuvrajsinghgmx.shopsmart.datastore.Poduct
import com.yuvrajsinghgmx.shopsmart.datastore.saveItems
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

data class Product(val name: String, val amount: Int, val imageUrl: String? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ShoppingListViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val items = viewModel.items.collectAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val selectedItems = remember { mutableStateListOf<Product>() }
    var showDeleteButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Color theme
    val lightBackgroundColor = Color(0xFFF6F5F3)
    val lightTextColor = Color(0xFF332D25)
    val primaryColor = Color(0xFF332D25)
    val secondaryColor = Color(0xFFDBD6CA)

    LaunchedEffect(viewModel) {
        viewModel.loadItems(context)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ShopSmart",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = lightTextColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBackgroundColor
                ),
                actions = {
                    if (showDeleteButton) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                val updatedItems =
                                    items.value.toMutableList().also { it.removeAll(selectedItems) }
                                viewModel.updateItems(updatedItems)
                                saveItems(context, updatedItems.map { Poduct(it.name, it.amount) })
                                selectedItems.clear()
                                showDeleteButton = false
                            }
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Selected",
                                tint = Color(primaryColor.value)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = primaryColor
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = lightBackgroundColor
                )
            }
        },
        bottomBar = {
            if (items.value.isNotEmpty()) {
                Surface(
                    color = Color(0xFF4D6357),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 60.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val subtotal = items.value.sumOf { it.amount }
                        val deliveryFee = 0
                        val discount = 0

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total:",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = lightTextColor
                                )
                            )
                            val total = subtotal + deliveryFee - discount
                            Text(
                                "₹$total",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = lightTextColor
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                // Handle checkout action
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text("Checkout", color = lightBackgroundColor, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackgroundColor)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items.value) { product ->
                    var isChecked by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                BorderStroke(2.dp, Color(0xFF332D25)),
                                RoundedCornerShape(16.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = secondaryColor)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            if (product.imageUrl != null) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp,100.dp)
                                        .clip(CircleShape)
                                        .padding(end = 1.dp)
                                        .border(
                                            BorderStroke(2.dp, Color(0xFF332D25)),
                                            CircleShape
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = lightTextColor
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${product.amount}",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    isChecked = checked
                                    if (checked) {
                                        selectedItems.add(product)
                                    } else {
                                        selectedItems.remove(product)
                                    }
                                    showDeleteButton = selectedItems.isNotEmpty()
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = primaryColor
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFF6F5F3))
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    var itemName by remember { mutableStateOf("") }
                    var itemAmount by remember { mutableStateOf("") }

                    Text(
                        "Add New Item",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF332D25)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item Name", color = Color(0xFF332D25)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color(0xFF332D25),
                            focusedBorderColor = Color(0xFF332D25),
                            unfocusedBorderColor = Color(0xFFDBD6CA),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )

                    OutlinedTextField(
                        value = itemAmount,
                        onValueChange = { itemAmount = it },
                        label = { Text("Amount", color = Color(0xFF332D25)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color(0xFF332D25),
                            focusedBorderColor = Color(0xFF332D25),
                            unfocusedBorderColor = Color(0xFFDBD6CA),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBD6CA)),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel", color = Color(0xFF332D25))
                        }

                        Button(
                            onClick = {
                                if (itemName.isNotBlank() && itemAmount.isNotBlank()) {
                                    if (itemName.isNotBlank() && itemAmount.isNotBlank()) {
                                        isLoading = true
                                        coroutineScope.launch {
                                            val imageUrl =
                                                viewModel.searchImage(itemName) // Fetch image URL
                                            val amountValue = itemAmount.toIntOrNull() ?: 0
                                            val newProduct = Product(
                                                itemName,
                                                amountValue,
                                                imageUrl
                                            ) // Include imageUrl
                                            val updatedItems = items.value.toMutableList()
                                                .also { it.add(newProduct) }
                                            viewModel.updateItems(updatedItems)
                                            saveItems(
                                                context,
                                                updatedItems.map {
                                                    Poduct(
                                                        it.name,
                                                        it.amount,
                                                        imageUrl
                                                    )
                                                }) // Save imageUrl
                                            newItem = ""
                                            newAmount = ""
                                            isLoading = false
                                            showDialog = false
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF332D25))
                        ) {
                            Text("Add", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
