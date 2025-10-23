package com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.cart.CartViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.components.AllProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.FeaturedProductCard
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.UiEvent
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import java.util.Locale

@Composable
fun ShopDetail(
    sharedViewModel: SharedShopViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current

    val selectedShop by sharedViewModel.selectedShop.collectAsState()
    val shopDetails by sharedViewModel.shopDetails.collectAsState()
    val isLoading by sharedViewModel.loading.collectAsState()
    val error by sharedViewModel.error.collectAsState()
    val isSaved by sharedViewModel.isShopSaved.collectAsState()

    LaunchedEffect(selectedShop?.id) {
        selectedShop?.let { shop ->
            sharedViewModel.getShopDetails(shop.id)
            sharedViewModel.initialStateFavorite(shop.isFavorite)
        }
    }

    LaunchedEffect(Unit) {
        sharedViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.CallShop -> {}
                is UiEvent.ShareProduct -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading && shopDetails == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error != null && shopDetails == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "Something went wrong", color = MaterialTheme.colorScheme.error)
            }

            shopDetails == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No shop selected", style = MaterialTheme.typography.titleMedium)
            }

            else -> {
                val shop = shopDetails!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(shop.images.firstOrNull())
                                    .crossfade(true)
                                    .build(),
                                contentDescription = shop.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                    }
                                },
                                error = {
                                    androidx.compose.foundation.Image(
                                        painter = painterResource(R.drawable.error),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0x22000000),
                                                Color(0x77000000)
                                            )
                                        )
                                    )
                            )

                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(42.dp)
                                    .background(Color.White.copy(alpha = 0.85f), CircleShape)
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }

                            IconButton(
                                onClick = {
                                    sharedViewModel.toggleFavoriteShop(shop.id)
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(42.dp)
                                    .background(Color.White.copy(alpha = 0.85f), CircleShape)
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Save Shop",
                                    tint = if (isSaved) MaterialTheme.colorScheme.primary else Color.Black
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 16.dp, bottom = 16.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    LabelChip(
                                        text = shop.category.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        },
                                        backgroundColor = GreenPrimary
                                    )
                                    LabelChip(text = "${"%.2f".format(shop.distance ?: 0.0)} km", backgroundColor = Color(0xFF4DB6F7))
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = shop.name,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 2
                                )
                            }
                        }
                    }

                    if (isLoading) {
                        item {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .clickable { navController.navigate("reviewScreen/shop/${shop.id}") },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            tonalElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val filled = shop.averageRating.toInt().coerceAtMost(5)
                                repeat(filled) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                }
                                if (shop.averageRating - filled >= 0.5) {
                                    Icon(Icons.AutoMirrored.Filled.StarHalf, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "${"%.1f".format(shop.averageRating)} • ${shop.reviewsCount} reviews", style = MaterialTheme.typography.bodyMedium)

                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "See reviews",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ActionItem(icon = Icons.Filled.LocationOn, label = "Directions") {
                                val lat = shop.latitude
                                val lng = shop.longitude
                                if (lat != null && lng != null) {
                                    val gmmIntentUri =
                                        "geo:$lat,$lng?q=${Uri.encode(shop.address)}".toUri()
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    if (mapIntent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(mapIntent)
                                    } else {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri))
                                    }
                                } else {
                                    Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                                }
                            }

                            ActionItem(icon = Icons.Filled.Call, label = "Call") {
                                val phone = shop.phone
                                if (!phone.isNullOrBlank()) {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())
                                    context.startActivity(dialIntent)
                                } else {
                                    Toast.makeText(context, "Phone not available", Toast.LENGTH_SHORT).show()
                                }
                            }

                            ActionItem(icon = Icons.Filled.Share, label = "Share") {
                                val lat = shop.latitude
                                val lng = shop.longitude

                                val mapLink = if (lat != null && lng != null) {
                                    "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
                                } else {
                                    "https://maps.google.com/?q=$lat,$lng${Uri.encode(shop.address)}"
                                }

                                val imageUrl = shop.images.firstOrNull() ?: ""
                                val description = shop.description


                                val shareText = """
                                    Check out this shop: ${shop.name}
                                    
                                    $description
                                    
                                    View on Map:
                                    $mapLink
                                    
                                    Image:
                                    $imageUrl
                                    
                                    (Shared from ShopSmart)
                                """.trimIndent()

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share ${shop.name}"))
                            }
                        }
                    }

                    item { HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ) }

                    item {
                        InfoRow(icon = Icons.Filled.LocationOn, label = "Address", value = shop.address)
                        InfoRow(icon = Icons.Filled.Call, label = "Phone", value = shop.phone ?: "N/A")
                        InfoRow(icon = Icons.Filled.AccessTime, label = "Hours", value = "Mon-Sat: 8:00 AM - 9:00 PM")
                    }

                    item {
                        val description = shop.description
                        if (description.isNotBlank()) {
                            var isDescriptionExpanded by remember { mutableStateOf(false) }
                            val isLongDescription = description.length > 150

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .animateContentSize()
                                    .clickable(enabled = isLongDescription) {
                                        isDescriptionExpanded = !isDescriptionExpanded
                                    }
                            ) {
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = if (isLongDescription && !isDescriptionExpanded) 3 else Int.MAX_VALUE,
                                    overflow = if (isLongDescription && !isDescriptionExpanded) TextOverflow.Ellipsis else TextOverflow.Visible
                                )
                                if (isLongDescription) {
                                    Text(
                                        text = if (isDescriptionExpanded) "Read less" else "Read more",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .align(Alignment.End)
                                    )
                                }
                            }
                        }
                    }


                    if (!shop.featuredProducts.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "Featured Products",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.titleMedium
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(shop.featuredProducts) { product ->
                                    FeaturedProductCard(
                                        product = product,
                                        cartViewModel = cartViewModel,
                                        context = LocalContext.current,
                                        modifier = Modifier.width(180.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (!shop.allProducts.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "All Products",
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        item {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                maxItemsInEachRow = 2
                            ) {
                                shop.allProducts.forEach { product ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(bottom = 10.dp)
                                    ) {
                                        AllProductCard(
                                            product = product,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun LabelChip(text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.height(30.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value ?: "N/A",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }
    }
}