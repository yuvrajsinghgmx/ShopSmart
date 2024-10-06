package com.yuvrajsinghgmx.shopsmart.screens

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
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.datastore.Poduct
import com.yuvrajsinghgmx.shopsmart.datastore.saveItems
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

data class Product(val name: String, val amount: Int, val imageUrl: String? = null)

data class ButtonNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ShoppingListViewModel = hiltViewModel(),navController: NavController) {
    val context = LocalContext.current
    val items = viewModel.items.collectAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val selectedItems = remember { mutableStateListOf<Product>() }
    var showDeleteButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }


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
                    )
                },
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
//                                tint = Color(primaryColor.value)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Item",
                )
            }
        },
        bottomBar = {
            if (items.value.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                ) {

                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (items.value.isEmpty()) {
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
                        ), label = ""
                    )

                    Image(
                        painter = painterResource(id = if(isSystemInDarkTheme()) R.drawable.empty_dark else R.drawable.empty_light),
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
                        "Add items using the '+' button below.",
                        style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                    )
                }
            }
            else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items.value) { product ->
                        var isChecked by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    BorderStroke(1.dp, Color(0xFF332D25)),
                                    RoundedCornerShape(16.dp)
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(rgb(234, 235, 230)))

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
                                        isChecked = checked
                                        if (checked) {
                                            selectedItems.add(product)
                                        } else {
                                            selectedItems.remove(product)
                                        }
                                        showDeleteButton = selectedItems.isNotEmpty()
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.width(300.dp)) {
                    val subtotal = items.value.sumOf { it.amount }
                    val deliveryFee = 0
                    val discount = 0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val total = subtotal + deliveryFee - discount
                        Text(
                            "Total: ₹${total}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightTextColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text("Checkout", color = lightBackgroundColor, fontSize = 18.sp)
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

                                if(itemName.isBlank() && itemAmount.isBlank()) {
                                    Toast.makeText(context, "Please Enter valid Data", Toast.LENGTH_SHORT).show()
                                }
                                else if(itemName.isBlank()) {
                                    Toast.makeText(context, "Please Enter a valid Name", Toast.LENGTH_SHORT).show()
                                }
                                else if(itemAmount.isBlank()) {
                                    Toast.makeText(context, "Please Enter a valid Amount", Toast.LENGTH_SHORT).show()
                                }

                                    if (itemName.isNotBlank() && itemAmount.isNotBlank()) {
                                        isLoading = true
                                        coroutineScope.launch {
                                            val imageUrl =
                                                viewModel.searchImage(itemName)
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
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}
