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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    navController: NavController,
    onNavigateToOrders: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Cart", "Orders")

    Column(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBar(onBackClick = { navController.popBackStack() })
            Spacer(modifier = Modifier.height(16.dp))
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp),
                        color = MaterialTheme.colorScheme.primary
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
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> CartScreenContent(
                    viewModel = viewModel
                )
                1 -> OrdersScreenPlaceholder()
            }
        }
    }
}

@Composable
fun CartScreenContent(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel
) {
    val cart by viewModel.cart.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.getCart() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        when {
            isLoading && cart == null -> CircularProgressIndicator(Modifier.align(Alignment.Center))

            !isLoading && cart == null -> Text(
                "Unable to load cart",
                Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )

            !isLoading && cart!!.items.isEmpty() -> Text(
                "Your cart is empty",
                Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )

            cart != null -> {
                val grouped = cart!!.items.groupBy { it.shop_name }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
//                    if (isLoading) {
//                        item {
//                            CircularProgressIndicator(Modifier.align(Alignment.Center).fillMaxWidth())
//                        }
//                    }
//
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
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    shopItems.forEachIndexed { index, item ->
                                        CartItemRow(
                                            item = item,
                                            onIncrease = {
                                                viewModel.addToCart(context, item.product, item.quantity + 1)
                                            },
                                            onDecrease = {
                                                if (item.quantity > 1) {
                                                    viewModel.addToCart(context, item.product, item.quantity - 1)
                                                } else {
                                                    viewModel.removeFromCart(item.product, 1)
                                                }
                                            }
                                        )
                                        if (index < shopItems.size - 1) HorizontalDivider(
                                            Modifier,
                                            DividerDefaults.Thickness,
                                            DividerDefaults.color
                                        )
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
                                            containerColor = MaterialTheme.colorScheme.primary
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My Cart & Orders",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back"
            )
        }
    }
}