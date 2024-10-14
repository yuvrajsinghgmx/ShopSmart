package com.yuvrajsinghgmx.shopsmart.screens

import android.content.Context
import android.graphics.Color.rgb
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.datastore.Poduct
import com.yuvrajsinghgmx.shopsmart.datastore.saveItems
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import com.yuvrajsinghgmx.shopsmart.utils.SharedPrefsHelper
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

data class Product(val name: String, val amount: Int, val imageUrl: String? = null, val dateAdded: Long = System.currentTimeMillis())

private fun saveOrdersToSharedPreferences(context: Context, items: List<Product>) {
    try {
        val orders = items.map { Poduct(it.name, it.amount, it.imageUrl) }
        if (orders.isNotEmpty()) {
            SharedPrefsHelper.saveOrders(context, orders)
            Log.d("HomeScreen", "Orders saved: ${orders.size}")
            Toast.makeText(context, "Orders saved successfully", Toast.LENGTH_SHORT).show()
        } else {
            SharedPrefsHelper.saveOrders(context, emptyList())
            Log.d("HomeScreen", "No orders to save, cleared existing orders")
            Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("HomeScreen", "Error saving orders", e)
        Toast.makeText(context, "Error saving orders", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ShoppingListViewModel = hiltViewModel(), navController: NavController) {
    val context = LocalContext.current
    val items = viewModel.items.collectAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val selectedItems = remember { mutableStateListOf<Product>() }
    var showDeleteButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var selectAll by remember { mutableStateOf(false) }

    var isSearching by remember{ mutableStateOf(false)}
    var searchKeyword by remember{ mutableStateOf("")}

    LaunchedEffect(viewModel) {
        viewModel.loadItems(context)
    }

    Scaffold(
        topBar = {
            if (selectedItems.isEmpty()) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu Icon")
                        }
                    },
                    title = {
                        if(isSearching){
                            OutlinedTextField(
                                value = searchKeyword,
                                onValueChange = {
                                    searchKeyword = it
                                    viewModel.search(searchKeyword)
                                                },
                                leadingIcon = {IconButton(onClick = {isSearching = false}) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back Arrow"
                                    )
                                }},
                                shape = RoundedCornerShape(25.dp)
                            )
                        }
                        else {
                            Text(
                                text = "ShopSmart",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearching = true
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search Icon")
                        }
                        Checkbox(
                            checked = selectAll,
                            onCheckedChange = { checked ->
                                selectAll = checked
                                if (checked) {
                                    selectedItems.clear() // Clear to avoid duplicates
                                    selectedItems.addAll(items.value)
                                } else {
                                    selectedItems.clear()
                                }
                                showDeleteButton = selectedItems.isNotEmpty()
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                )
            } else {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            selectedItems.clear()
                            selectAll = false
                            showDeleteButton = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Icon")
                        }
                    },
                    title = {
                        Text(
                            text = "${selectedItems.size} selected",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    actions = {
                        if (showDeleteButton) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    val updatedItems = items.value.toMutableList().apply {
                                        removeAll(selectedItems)
                                    }
                                    viewModel.updateItems(updatedItems)
                                    selectedItems.clear()
                                    selectAll = false
                                    showDeleteButton = false
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Icon")
                            }
                        }
                    }
                )
            }
        },

        floatingActionButton = {
            val subtotal = selectedItems.sumOf { it.amount }
            val deliveryFee = 0
            val discount = 0
            val total = subtotal + deliveryFee - discount
            if (selectedItems.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        val selectedItemsJson = Gson().toJson(selectedItems)
                        navController.navigate("MyOrders?selectedItems=$selectedItemsJson")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp).width(150.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Total:"
                            )

                            Text(
                                "₹${total}",
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Checkout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                    }
                }
            } else {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(5.dp, 0.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Item",
                    )
                }
            }
        }


    ) {innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (items.value.isEmpty())
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Image(
                            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.empty_dark else R.drawable.empty_light),
                            contentDescription = "Empty List",
                            modifier = Modifier
                                .size(200.dp)
                                .scale(scale)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Your shopping list is empty.",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Add items using the  button below.",
                            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                        )
                    }
                }
            } else
            {
                val groupedItems = items.value.groupBy { product ->
                    val date = java.util.Date(product.dateAdded)
                    val dateFormat = java.text.SimpleDateFormat("EEEE, d/M/y", java.util.Locale.getDefault())
                    dateFormat.format(date)
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    groupedItems.forEach { (day, products) ->
                        item {
                            Text(
                                text = day,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light,
                                ),
                                modifier = Modifier.padding(8.dp,3.dp,0.dp,3.dp)
                            )
                        }
                        items(products) { product ->
                            val isChecked = product in selectedItems
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(2.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                ) {
                                    if (product.imageUrl != null) {
                                        AsyncImage(
                                            model = product.imageUrl,
                                            contentDescription = product.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(70.dp, 70.dp)
                                                .clip(CircleShape)
                                                .padding(end = 1.dp)
                                                .border(
                                                    BorderStroke(1.dp, Color(0xFF332D25)),
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
                                            if (checked) {
                                                selectedItems.add(product)
                                            } else {
                                                selectedItems.remove(product)
                                            }
                                            showDeleteButton = selectedItems.isNotEmpty()
                                            selectAll = selectedItems.size == items.value.size
                                        },
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
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
                    .background(Color.Transparent),
                colors = CardDefaults.cardColors(
                    containerColor = Color(rgb(234, 235, 230))
                )
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
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item Name", color = Color.Black) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color(0xFF332D25),
                            focusedBorderColor = Color(0xFF332D25),
                            unfocusedBorderColor = Color(0xFFDBD6CA),
                            focusedTextColor = Color(0xFF332D25),
                            unfocusedTextColor = Color(0xFF332D25)
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )

                    OutlinedTextField(
                        value = itemAmount,
                        onValueChange = { itemAmount = it },
                        label = { Text("Amount", color = Color.Black) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color(0xFF332D25),
                            focusedBorderColor = Color(0xFF332D25),
                            unfocusedBorderColor = Color(0xFFDBD6CA),
                            focusedTextColor = Color(0xFF332D25),
                            unfocusedTextColor = Color(0xFF332D25)

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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF332D25)),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel", color = Color.White)
                        }

                        Button(
                            onClick = {
                                if (itemName.isBlank() && itemAmount.isBlank()) {
                                    Toast.makeText(context, "Please Enter valid Data", Toast.LENGTH_SHORT).show()
                                } else if (itemName.isBlank()) {
                                    Toast.makeText(context, "Please Enter a valid Name", Toast.LENGTH_SHORT).show()
                                } else if (itemAmount.isBlank()) {
                                    Toast.makeText(context, "Please Enter a valid Amount", Toast.LENGTH_SHORT).show()
                                }

                                if (itemName.isNotBlank() && itemAmount.isNotBlank()) {
                                    isLoading = true
                                    coroutineScope.launch {
                                        val imageUrl = viewModel.searchImage(itemName)
                                        val amountValue = itemAmount.toIntOrNull() ?: 0
                                        val newProduct = Product(
                                            name = itemName,
                                            amount = amountValue,
                                            imageUrl = imageUrl,
                                            dateAdded = System.currentTimeMillis() // Set the current time
                                        )
                                        val updatedItems = items.value.toMutableList().also { it.add(newProduct) }
                                        viewModel.updateItems(updatedItems)
                                        saveItems(context, updatedItems.map { Poduct(it.name, it.amount, it.imageUrl, it.dateAdded) })
                                        newItem = ""
                                        newAmount = ""
                                        isLoading = false
                                        showDialog = false
                                    }
                                }
                            }
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

