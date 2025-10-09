package com.yuvrajsinghgmx.shopsmart.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateToCheckout: () -> Unit = {},
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        bottomBar = {
            if (uiState.cartItems.isNotEmpty()) {
                CartBottomBar(
                    totalAmount = uiState.totalAmount,
                    selectedItemsCount = uiState.selectedItemsCount,
                    onCheckout = onNavigateToCheckout
                )
            }
        },
        containerColor = colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorScheme.primary)
                    }
                }

                uiState.cartItems.isEmpty() -> {
                    EmptyCartContent()
                }

                else -> {
                    CartContent(
                        cartItems = uiState.cartItems,
                        onQuantityChange = { productId, quantity ->
                            viewModel.updateQuantity(productId, quantity)
                        },
                        onToggleSelection = { productId ->
                            viewModel.toggleItemSelection(productId)
                        },
                        onRemoveItem = { productId ->
                            viewModel.removeItem(productId)
                        },
                        totalItemCount = uiState.cartItems.size
                    )
                }
            }
        }
    }
}

@Composable
private fun CartHeading(
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Cart",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
        if (itemCount > 0) {
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                shape = CircleShape,
                color = colorScheme.primary,
                modifier = Modifier.size(30.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemCount.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CartContent(
    cartItems: List<CartItem>,
    onQuantityChange: (String, Int) -> Unit,
    onToggleSelection: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    totalItemCount: Int
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CartHeading(
            itemCount = totalItemCount,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp) // Adjusted padding
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ITEMS
            items(cartItems, key = { it.product.productId }) { cartItem ->
                CartItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    cartItem = cartItem,
                    onQuantityChange = onQuantityChange,
                    onToggleSelection = onToggleSelection,
                    onRemoveItem = onRemoveItem
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onQuantityChange: (productId: String, newQuantity: Int) -> Unit,
    onToggleSelection: (productId: String) -> Unit,
    onRemoveItem: (productId: String) -> Unit
) {
    val product = cartItem.product
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surfaceVariant)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(product.imageUrl.firstOrNull()),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .size(24.dp)
                            .clickable { onToggleSelection(product.productId) },
                        shape = RoundedCornerShape(4.dp),
                        color = if (cartItem.isSelected) colorScheme.primary else colorScheme.surfaceVariant,
                        shadowElevation = 2.dp
                    ) {
                        if (cartItem.isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp).align(Alignment.Center)
                            )
                        } else {
                            // Blank box when not selected
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f).height(100.dp), // Match image height
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = product.price, // Unit Price
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorScheme.primary
                        )
                    }

                    Spacer(Modifier.height(3.dp))

                    QuantityControlRow(
                        quantity = cartItem.quantity,
                        onIncrement = { onQuantityChange(product.productId, cartItem.quantity + 1) },
                        onDecrement = { onQuantityChange(product.productId, cartItem.quantity - 1) },
                        colorScheme = colorScheme
                    )
                }
            }

            IconButton(
                onClick = { onRemoveItem(product.productId) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp) // Padding from card edge
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Item",
                    tint = colorScheme.error.copy(alpha = 0.8f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${product.shopName} · ${product.distance}",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuantityControlRow(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    colorScheme: ColorScheme
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, colorScheme.outlineVariant, RoundedCornerShape(8.dp))
    ) {
        // Decrement Button
        IconButton(
            onClick = onDecrement,
            enabled = quantity > 1,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease Quantity",
                tint = if (quantity > 1) colorScheme.primary else colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "$quantity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(
            onClick = onIncrement,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase Quantity",
                tint = colorScheme.primary
            )
        }
    }
}

@Composable
private fun CartBottomBar(
    totalAmount: Double,
    selectedItemsCount: Int,
    onCheckout: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total ($selectedItemsCount items):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = "$${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedItemsCount > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Proceed to Checkout",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCartContent() {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Empty Cart",
                modifier = Modifier.size(120.dp),
                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Discover amazing deals and add items to your cart",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Navigate to products */ },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Start Shopping",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

