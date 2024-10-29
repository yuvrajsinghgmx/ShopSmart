package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class Brand(
    val name: String,
    val logo: Int,
    val category: String,
    var isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandFavoritesScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }

    // Sample brands data
    var brands by remember {
        mutableStateOf(
            listOf(
                Brand("Nike", R.drawable.brand_sports_24px, "Sports", true),
                Brand("Adidas", R.drawable.brand_sports_24px, "Sports", false),
                Brand("Puma", R.drawable.brand_sports_24px, "Sports", true),
                Brand("Samsung", R.drawable.brand_electronics_24px, "Electronics", true),
                Brand("Apple", R.drawable.brand_electronics_24px, "Electronics", false),
                Brand("Sony", R.drawable.brand_electronics_24px, "Electronics", true),
                Brand("H&M", R.drawable.brand_fashion_24px, "Fashion", false),
                Brand("Zara", R.drawable.brand_fashion_24px, "Fashion", true),
                Brand("Uniqlo", R.drawable.brand_fashion_24px, "Fashion", false),
                Brand("IKEA", R.drawable.brand_home_24px, "Home", true),
                Brand("Dyson", R.drawable.brand_home_24px, "Home", false),
                Brand("Philips", R.drawable.brand_home_24px, "Home", true)
            )
        )
    }

    val categories = brands.map { it.category }.distinct()
    val filteredBrands = brands.filter { brand ->
        (selectedCategory == null || brand.category == selectedCategory) &&
                brand.name.contains(searchQuery, ignoreCase = true)
    }
    val favoriteBrands = filteredBrands.filter { it.isFavorite }
    val suggestedBrands = filteredBrands.filter { !it.isFavorite }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Brand Favorites",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (favoriteBrands.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.clear_24px),
                                contentDescription = "Clear Favorites"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBackgroundColor
                )
            )
        },
        containerColor = lightBackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search brands") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search_24px),
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close_24px),
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Category Filter
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory) + 1,
                modifier = Modifier.padding(horizontal = 16.dp),
                edgePadding = 0.dp,
                divider = {},
                containerColor = lightBackgroundColor
            ) {
                Tab(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    text = { Text("All") }
                )
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Favorite Brands Section
                if (favoriteBrands.isNotEmpty()) {
                    item {
                        Text(
                            text = "Favorite Brands",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height((favoriteBrands.size / 2 * 120).dp)
                        ) {
                            items(favoriteBrands) { brand ->
                                BrandCard(
                                    brand = brand,
                                    onToggleFavorite = {
                                        brands = brands.map {
                                            if (it.name == brand.name) it.copy(isFavorite = !it.isFavorite)
                                            else it
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Suggested Brands Section
                if (suggestedBrands.isNotEmpty()) {
                    item {
                        Text(
                            text = "Suggested Brands",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height((suggestedBrands.size / 2 * 120).dp)
                        ) {
                            items(suggestedBrands) { brand ->
                                BrandCard(
                                    brand = brand,
                                    onToggleFavorite = {
                                        brands = brands.map {
                                            if (it.name == brand.name) it.copy(isFavorite = !it.isFavorite)
                                            else it
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Empty State
                if (filteredBrands.isEmpty()) {
                    item {
                        EmptyState()
                    }
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Clear Favorites") },
                text = { Text("Are you sure you want to remove all favorite brands?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            brands = brands.map { it.copy(isFavorite = false) }
                            showClearDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Clear All")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrandCard(
    brand: Brand,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        // Remove the onClick from the Card
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = brand.logo),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = brand.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            // Wrap the IconButton in a Box to prevent click propagation
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // No ripple effect
                        onClick = onToggleFavorite
                    )
            ) {
                Icon(
                    painter = painterResource(
                        id = if (brand.isFavorite) R.drawable.favorite_filled_24px
                        else R.drawable.favorite_border_24px
                    ),
                    contentDescription = if (brand.isFavorite) "Remove from favorites"
                    else "Add to favorites",
                    tint = if (brand.isFavorite) Color(0xFF0E8545) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.favorite_border_24px),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No brands found",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Try adjusting your filters or search terms",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}