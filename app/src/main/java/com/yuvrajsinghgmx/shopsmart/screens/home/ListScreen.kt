package com.yuvrajsinghgmx.shopsmart.screens.home

import android.graphics.Color.rgb
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.datastore.Product
import com.yuvrajsinghgmx.shopsmart.datastore.saveItems
import com.yuvrajsinghgmx.shopsmart.ui.theme.LexendRegular
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ShoppingListViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val items = viewModel.items.collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var selectAll by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var searchKeyword by remember { mutableStateOf("") }
    val selectedItems = remember { mutableStateListOf<Product>() }
    val coroutineScope = rememberCoroutineScope()

    // for search bar entry animation
    val animatedSize by animateFloatAsState(
        targetValue = if (isSearching) 1f else 0f,
        animationSpec = tween(durationMillis = 300), label = "" // Adjust duration as needed
    )

    // Calculate total for selected items
    val totalAmount = selectedItems.sumOf { it.amount * it.no_of_items }

    // Function to update item quantity
    fun updateItemQuantity(product: Product, newQuantity: Int) {
        if (newQuantity <= 0) {
            showDeleteDialog = true
            productToDelete = product
            return
        }

        coroutineScope.launch {
            val updatedItems = items.value.map {
                if (it == product) {
                    it.copy(no_of_items = newQuantity)
                } else {
                    it
                }
            }
            viewModel.updateItems(updatedItems)
            saveItems(context, updatedItems)

            // Update selected items if the modified item is selected
            val selectedIndex = selectedItems.indexOf(product)
            if (selectedIndex != -1) {
                selectedItems[selectedIndex] = selectedItems[selectedIndex].copy(no_of_items = newQuantity)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5F3))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF6F5F3),
                shadowElevation = 1.dp
            ) {
                if (selectedItems.isEmpty()) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "back Icon", tint = Color(0xFF98F9B3))
                            }
                        },
                        title = {
                            if (isSearching) {
                                OutlinedTextField(
                                    value = searchKeyword,
                                    onValueChange = {
                                        searchKeyword = it
                                        viewModel.search(searchKeyword)
                                    },
                                    leadingIcon = {
                                        IconButton(onClick = {
                                            isSearching = false
                                            searchKeyword = ""
                                            viewModel.search("")
                                        }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back Arrow",
                                                tint = Color(0xFF98F9B3)
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(25.dp),
                                    modifier = Modifier.fillMaxWidth().scale(animatedSize),
                                    placeholder = { Text("Search") }
                                )
                            } else {
                                Text(
                                    text = "Shopping List",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        },
                        actions = {
                            if (!isSearching) {
                                IconButton(
                                    onClick = { isSearching = true },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(0xAB98F9B3)
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search Icon",
                                        tint = Color(0xFF006D3B)
                                    )
                                }
                            }
                            Checkbox(
                                checked = selectAll,
                                onCheckedChange = { checked ->
                                    selectAll = checked
                                    if (checked) {
                                        selectedItems.clear()
                                        selectedItems.addAll(items.value)
                                    } else {
                                        selectedItems.clear()
                                    }
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = CheckboxDefaults.colors(uncheckedColor = Color(0xFF98F9B3))
                            )
                        }
                    )
                } else {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                selectedItems.clear()
                                selectAll = false
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
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    val updatedItems = items.value.toMutableList().apply {
                                        removeAll(selectedItems)
                                    }
                                    viewModel.updateItems(updatedItems)
                                    saveItems(context, updatedItems)
                                    selectedItems.clear()
                                    selectAll = false
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Icon")
                            }
                        }
                    )
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (items.value.isEmpty()) {
                    EmptyListContent()
                } else {
                    val groupedItems = items.value.groupBy { product ->
                        val date = java.util.Date(product.dateAdded)
                        val dateFormat = java.text.SimpleDateFormat("EEEE, d/M/y", java.util.Locale.getDefault())
                        dateFormat.format(date)
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = if (selectedItems.isNotEmpty()) 90.dp else 0.dp)
                    ) {
                        groupedItems.forEach { (day, products) ->
                            item {
                                Text(
                                    text = day,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Light,
                                    ),
                                    modifier = Modifier.padding(8.dp, 3.dp, 0.dp, 3.dp)
                                )
                            }
                            items(products.size) { index ->
                                ProductCard(
                                    product = products[index],
                                    isSelected = products[index] in selectedItems,
                                    onQuantityChange = { product, newQuantity ->
                                        updateItemQuantity(product, newQuantity)
                                    },
                                    onSelect = { product ->
                                        if (product in selectedItems) {
                                            selectedItems.remove(product)
                                            selectAll = false
                                        } else {
                                            selectedItems.add(product)
                                            selectAll = selectedItems.size == items.value.size
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // FAB
        if (selectedItems.isEmpty()) {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF98F9B3),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = Color.Black
                )
            }
        }

        // Bottom Card
        if (selectedItems.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF98F9B3)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total:",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = LexendRegular,
                                color = Color.Black
                            )
                        )
                        Text(
                            "₹${String.format("%.1f", totalAmount)}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = LexendRegular,
                                color = Color.Black
                            )
                        )
                    }
                    Button(
                        onClick = {
                            val selectedItemsJson = Gson().toJson(selectedItems)
                            navController.navigate("checkout?selectedItems=$selectedItemsJson")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006D3B)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Checkout",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = LexendRegular,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }

    // Keep all existing dialogs
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remove Item") },
            text = { Text("Do you want to remove this item from your list?") },
            confirmButton = {
                TextButton(onClick = {
                    productToDelete?.let { product ->
                        coroutineScope.launch {
                            val updatedItems = items.value.toMutableList()
                            updatedItems.remove(product)
                            viewModel.updateItems(updatedItems)
                            saveItems(context, updatedItems)
                            selectedItems.remove(product)
                        }
                    }
                    showDeleteDialog = false
                    productToDelete = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    productToDelete?.let { product ->
                        updateItemQuantity(product, 1)
                    }
                    showDeleteDialog = false
                    productToDelete = null
                }) {
                    Text("No")
                }
            }
        )
    }

    if (showDialog) {
        AddItemDialog(
            onDismiss = { showDialog = false },
            onAddItem = { name, amount, date ->
                coroutineScope.launch {
                    val imageUrl = viewModel.searchImage(name)
                    val newProduct = Product(
                        name = name,
                        amount = amount,
                        no_of_items = 1,
                        imageUrl = imageUrl,
                        dateAdded = date
                    )
                    val updatedItems = items.value.toMutableList().also { it.add(newProduct) }
                    viewModel.updateItems(updatedItems)
                    saveItems(context, updatedItems)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    isSelected: Boolean,
    onQuantityChange: (Product, Int) -> Unit,
    onSelect: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect(product) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        .size(65.dp, 65.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, color = Color.Transparent),
                            RoundedCornerShape(8.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontFamily = LexendRegular
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Fresh and ripe",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontFamily = LexendRegular
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "₹${String.format("%.1f", product.amount * product.no_of_items)}",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color(0xFF48BFE3),
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendRegular
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .width(90.dp)
                    .padding(start = 8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp)
                        .clickable {
                            onQuantityChange(product, product.no_of_items - 1)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.grey))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.decrement_icon),
                            contentDescription = "Decrease quantity",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = product.no_of_items.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp)
                        .clickable {
                            onQuantityChange(product, product.no_of_items + 1)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.grey))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.increment_icon),
                            contentDescription = "Increase quantity",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyListContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            ),
            label = ""
        )

        Image(
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.empty_dark
                else R.drawable.empty_light
            ),
            contentDescription = "Empty List",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your shopping list is empty.",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add items using the + button below.",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color.Gray,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (name: String, amount: Double, date: Long) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) } // Default to current date
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
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
                onValueChange = {
                    itemName = it
                    showError = false
                },
                label = { Text("Item Name", color = Color.Black) },
                shape = RoundedCornerShape(8.dp),
                isError = showError && itemName.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(0xFF006D3B),
                    focusedBorderColor = Color(0xFF006D3B),
                    unfocusedBorderColor = Color(0xFFDBD6CA),
                    focusedTextColor = Color(0xFF332D25),
                    unfocusedTextColor = Color(0xFF332D25),
                    errorBorderColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            OutlinedTextField(
                value = itemAmount,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        itemAmount = newValue
                        showError = false }
                },
                label = { Text("Amount (optional)", color = Color.Black) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(8.dp),
                isError = showError && (itemAmount.isNotBlank() && itemAmount.toDoubleOrNull() == null),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(0xFF006D3B),
                    focusedBorderColor = Color(0xFF006D3B),
                    unfocusedBorderColor = Color(0xFFDBD6CA),
                    focusedTextColor = Color(0xFF332D25),
                    unfocusedTextColor = Color(0xFF332D25),
                    errorBorderColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D3B)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Select Date")
            }

            Text(
                "Selected Date: ${java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate)}",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF006D3B)
            )


            if (showError) {
                Text(
                    text = "Please fill all fields correctly",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF332D25)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", color = Color.White)
                }

                Button(
                    onClick = {
                        val amount = itemAmount.toDoubleOrNull() ?: 0.0
                        if (itemName.isBlank()|| (amount != null && amount < 0)) {
                            showError = true
                        } else {
                            onAddItem(itemName, amount ?:0.0, selectedDate!!)
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D3B)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add")
                }
            }
        }
    }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis() // Default to current date if null
                        showDatePicker = false
                        showError = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}}
