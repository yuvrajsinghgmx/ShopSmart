package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ShopCard
import com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedShopViewModel,
    navController: NavController,
    authPrefs: AuthPrefs
) {
    val state = viewModel.state.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {

        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hi, ${authPrefs.getName()}",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Showing shops within 10 km",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                AsyncImage(
                    model = authPrefs.getProfilePic(),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable { },
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Search Bar is already setup
        item {
            OutlinedTextField(
                value = state.searchQuery ?: "",
                onValueChange = { viewModel.onEvent(HomeEvent.Search(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filter") },
                placeholder = { Text("Search shops or products...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // Banner
        item {
            AsyncImage(
                model = "https://picsum.photos/800/200",
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        // Trending Products
        item {
            Column {
                Text(
                    text = "Trending Products",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.products) { product ->
                        ProductCard(
                            product = product,
                            onClick = {
                                navController.navigate("productScreen/${product.id}")
                            }
                        )
                    }
                }
            }
        }

        // Nearby Shops Horizontal Row
        item {
            Text(
                text = "Nearby Shops",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.nearbyShops) { shop ->
                    ShopCard(
                        shop = shop,
                        onClick = {
                            sharedViewModel.setSelectedShop(shop)
                            navController.navigate("shopDetails")
                        }
                    )
                }
            }
        }

        // Categorized Products (Grid)
        val categorized = state.categorizedProducts
        for (i in categorized.indices) {
            val category = categorized[i]
            item {
                Column {
                    Text(
                        text = category.type,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // two-column grid using chunked(2)
                    val rows = category.items.chunked(2)
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // For each product in the row, give equal weight
                            row.forEach { product ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            navController.navigate("productScreen/${product.id}")
                                        }
                                    )
                                }
                            }
                            // If single item in this row, add an empty spacer box to balance columns
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}