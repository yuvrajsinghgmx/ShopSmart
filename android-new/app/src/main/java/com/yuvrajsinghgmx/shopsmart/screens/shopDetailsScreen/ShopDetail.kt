package com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.cart.CartViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.components.AllProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.FeaturedProductCard
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.UiEvent
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun ShopDetail(
    sharedViewModel: SharedShopViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val selectedShop = sharedViewModel.selectedShop.collectAsState().value
    val shopDetails = sharedViewModel.shopDetails.collectAsState().value
    val isLoading = sharedViewModel.loading.collectAsState().value
    val error = sharedViewModel.error.collectAsState().value
    val isSaved by sharedViewModel.isShopSaved.collectAsState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch shop details
    LaunchedEffect(selectedShop) {
        selectedShop?.let {
            sharedViewModel.getShopDetails(it.id)
            sharedViewModel.initialStateFavorite(it.isFavorite)
        }
    }

    // Toast events
    LaunchedEffect(Unit) {
        sharedViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is UiEvent.CallShop -> TODO()
                is UiEvent.ShareProduct -> TODO()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) } // ✅ Snackbar host placed here
    ) { padding ->

        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error, color = Color.Red)
            }

            shopDetails == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No shop selected", style = MaterialTheme.typography.titleLarge)
            }

            else -> {
                val shop = shopDetails

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(padding)
                ) {
                    // --- HEADER IMAGE ---
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(shop.images.firstOrNull())
                                    .crossfade(true)
                                    .build(),
                                contentDescription = shop.name,
                                placeholder = painterResource(R.drawable.error),
                                error = painterResource(R.drawable.error),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(36.dp)
                                    .background(Color(0x66FFFFFF), CircleShape)
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    LabelChip(text = shop.category, backgroundColor = GreenPrimary)
                                    LabelChip(
                                        text = "${shop.distance ?: 0.0} km away",
                                        backgroundColor = Color(0xFF4DB6F7)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = shop.name,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 48.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    // --- RATING ---
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF888888))
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .clickable {
                                    navController.navigate("reviewScreen/shop/${shop.id}")
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(4) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFD700)
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.StarHalf,
                                contentDescription = "Half Star",
                                tint = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${shop.averageRating}", fontSize = 16.sp)
                        }
                    }

                    // --- ACTIONS ---
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 45.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ActionItem(icon = Icons.Filled.LocationOn, label = "Directions")
                            ActionItem(icon = Icons.Filled.Call, label = "Call")
                            ActionItem(icon = Icons.Filled.Share, label = "Share")
                        }
                    }

                    item { HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0)) }

                    // --- INFO ---
                    item {
                        InfoRow(icon = Icons.Filled.LocationOn, label = "Address", value = shop.address)
                        InfoRow(icon = Icons.Filled.Call, label = "Phone", value = shop.phone)
                        InfoRow(
                            icon = Icons.Filled.AccessTime,
                            label = "Hours",
                            value = "Mon-Sat: 8:00 AM - 9:00 PM"
                        )
                    }

                    item {
                        Text(
                            text = shop.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // --- FEATURED PRODUCTS ---
                    val featuredProducts = shop.featuredProducts
                    if (!featuredProducts.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "Featured Products",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.headlineLarge
                            )

                            LazyRow(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                items(featuredProducts) { product ->
                                    FeaturedProductCard(
                                        product = product,
                                        cartViewModel = cartViewModel,
                                        snackbarHostState = snackbarHostState,
                                        coroutineScope = coroutineScope
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    // --- ALL PRODUCTS ---
                    val allProducts = shop.allProducts
                    item {
                        Text(
                            text = "All Products",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }

                    if (!allProducts.isNullOrEmpty()) {
                        items(allProducts.chunked(2)) { rowProducts ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowProducts.forEach { product ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        AllProductCard(product)
                                    }
                                }
                                if (rowProducts.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "No products available",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // --- ACTION BUTTONS ---
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text("Call Shop", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { sharedViewModel.toggleFavoriteShop(shop.id) },
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Save button",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = if (isSaved) "Saved" else "Save Shop",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun LabelChip(text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor, modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            color = Color.White, style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF2121D2),
            modifier = Modifier.size(28.dp)
        )
        Text(text = label, fontSize = 14.sp, color = Color(0xFF2121D2))
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF2121D2),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

