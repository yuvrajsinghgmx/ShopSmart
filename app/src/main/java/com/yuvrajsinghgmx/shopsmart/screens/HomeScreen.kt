package com.yuvrajsinghgmx.shopsmart.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.datastore.Poduct
import com.yuvrajsinghgmx.shopsmart.datastore.saveItems
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

data class Product(val name: String, val amount: Int)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ShoppingListViewModel = viewModel()) {
    val context = LocalContext.current
    val items = viewModel.items.collectAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val selectedItems = remember { mutableStateListOf<Product>() }
    var showDeleteButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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
                                val updatedItems = items.value.toMutableList().also { it.removeAll(selectedItems) }
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
                Icon(Icons.Default.Add, contentDescription = "Add Item", tint = lightBackgroundColor)
            }
        },
        bottomBar = {
            if (items.value.isNotEmpty()) {
                Surface(
                    color = lightBackgroundColor,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val subtotal = items.value.sumOf { it.amount }
                        val deliveryFee = 10
                        val discount = 0

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = lightTextColor))
                            val total = subtotal + deliveryFee - discount
                            Text("₹$total", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = lightTextColor))
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
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, secondaryColor),
                        colors = CardDefaults.cardColors(containerColor = lightBackgroundColor)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.shoppingbag),
                                contentDescription = "Placeholder",
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(end = 8.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = lightTextColor
                                    )
                                )
                                Text(
                                    text = "₹${product.amount}",
                                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                                )
                            }
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
                                )
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
                modifier = Modifier.padding(16.dp).background(Color(0xFFF6F5F3))
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
                                    val amountValue = itemAmount.toIntOrNull() ?: 0
                                    coroutineScope.launch {
                                        val newProduct = Product(itemName, amountValue)
                                        val updatedItems =
                                            items.value.toMutableList().also { it.add(newProduct) }
                                        viewModel.updateItems(updatedItems)
                                        saveItems(
                                            context,
                                            updatedItems.map { Poduct(it.name, it.amount) })
                                        newItem = ""
                                        newAmount = ""
                                    }
                                }
                                showDialog = false
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