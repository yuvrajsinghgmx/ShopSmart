package com.yuvrajsinghgmx.shopsmart.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateToOrders: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Cart", "Orders")

    Scaffold(
        topBar = {
            Column {
                TopBar(navController = NavController(LocalContext.current))
                Spacer(modifier = Modifier.height(16.dp))
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF00C853),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .height(3.dp),
                            color = Color(0xFF00C853)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                if (index == 1) onNavigateToOrders()
                            },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTab == index)
                                        Color(0xFF00C853)
                                    else
                                        Color.Gray,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = if (selectedTab == index)
                                        FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> CartScreenContent(
                modifier = Modifier.padding(padding),
                viewModel = viewModel
            )

            1 -> OrdersScreenPlaceholder(modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun CartScreenContent(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel
) {
    val cart by viewModel.cart.collectAsState(initial = null)
    val isLoading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) { viewModel.getCart() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

            cart == null -> Text(
                "Unable to load cart",
                Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )

            cart!!.items.isEmpty() -> Text(
                "Your cart is empty",
                Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )

            else -> {
                val grouped = cart!!.items.groupBy { it.shop_name }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    grouped.forEach { (shopName, shopItems) ->
                        item {
                            Text(
                                text = shopName,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                modifier = Modifier.fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    shopItems.forEachIndexed { index, item ->
                                        CartItemRow(
                                            item = item,
                                            onIncrease = {
                                                viewModel.addToCart(item.product, item.quantity + 1)
                                            },
                                            onDecrease = {
                                                if (item.quantity > 1) {
                                                    viewModel.addToCart(item.product, item.quantity - 1)
                                                } else {
                                                    viewModel.removeFromCart(item.product, 1)
                                                }
                                            }
                                        )
                                        if (index < shopItems.size - 1) Divider()
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    val total = shopItems.sumOf {
                                        it.price.toDoubleOrNull()?.times(it.quantity) ?: 0.0
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.createOrder(
                                                shopId = shopItems.first().shop_id,
                                                address = "Default Address"
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00C853)
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Place Order  •  ₹${"%.2f".format(total)}",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

//                                    TextButton(
//                                        onClick = {
//                                            viewModel.clearCart(shopItems.first().shop_id)
//                                        },
//                                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                                    ) {
//                                        Text(
//                                            "Remove All from Shop",
//                                            color = Color.Gray
//                                        )
//                                    }
                                }
                            }

                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersScreenPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your Orders will appear here",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My Cart & Orders",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick =
                { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back"
            )
        }
    }
}