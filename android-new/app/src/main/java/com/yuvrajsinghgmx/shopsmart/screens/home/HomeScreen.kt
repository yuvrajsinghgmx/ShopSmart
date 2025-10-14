package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.CategorizedProductsUi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop
import com.yuvrajsinghgmx.shopsmart.screens.home.components.HomeFooter
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ShopCard
import com.yuvrajsinghgmx.shopsmart.screens.shopDetailsScreen.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import kotlin.math.ceil

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedShopViewModel,
    navController: NavController,
    authPrefs: AuthPrefs
) {
    val state = viewModel.state.value

    Column(modifier = Modifier.fillMaxSize()) {
        HomeHeader(authPrefs = authPrefs)
        HomeSearchBar(
            onClick = {navController.navigate("search")
            }
        )
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            HomeBanner()

            if (state.products.isNotEmpty()) {
                ProductCarouselSection(
                    title = "Trending Products",
                    products = state.products,
                    navController = navController
                )
            }

            if (state.nearbyShops.isNotEmpty()) {
                ShopCarouselSection(
                    title = "Nearby Shops",
                    shops = state.nearbyShops,
                    onShopClick = { shop ->
                        sharedViewModel.setSelectedShop(shop)
                        navController.navigate("shopDetails")
                    }
                )
            }

            if (state.categorizedProducts.isNotEmpty()) {
                CategorizedProductsSection(
                    categorizedProducts = state.categorizedProducts,
                    navController = navController
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            HomeFooter()
        }
    }
}

@Composable
fun HomeHeader(authPrefs: AuthPrefs) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hi, ${authPrefs.getName()}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Showing shops within 10 km",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            )
        }
        AsyncImage(
            model = authPrefs.getProfilePic(),
            contentDescription = "Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { /* Navigate to profile */ },
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
fun HomeSearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search products or shops...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f) // Ensures text doesn't overlap the filter icon
            )
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
fun HomeBanner() {
    AsyncImage(
        model = "https://picsum.photos/800/200",
        contentDescription = "Banner",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
fun SectionHeader(title: String, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        TextButton(onClick = onViewAllClick) {
            Text(
                "See All",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun ProductCarouselSection(title: String, products: List<Product>, navController: NavController) {
    Column {
        SectionHeader(title = title, onViewAllClick = { /* TODO: Navigate to full product list */ })
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClick = { navController.navigate("productScreen/${product.id}") },
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}

@Composable
fun ShopCarouselSection(title: String, shops: List<Shop>, onShopClick: (Shop) -> Unit) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        SectionHeader(title = title, onViewAllClick = { /* TODO: Navigate to full shop list */ })
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            items(shops) { shop ->
                ShopCard(shop = shop, onClick = { onShopClick(shop) })
            }
        }
    }
}

@Composable
fun CategorizedProductsSection(
    categorizedProducts: List<CategorizedProductsUi>,
    navController: NavController
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        if (categorizedProducts.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                divider = {}
            ) {
                categorizedProducts.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = category.type, style = MaterialTheme.typography.bodyMedium) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val selectedCategoryProducts = categorizedProducts.getOrNull(selectedTabIndex)?.items ?: emptyList()
            if (selectedCategoryProducts.isNotEmpty()) {
                val minCardWidth = 160.dp

                val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
                val availableWidth = screenWidthDp - 32.dp
                val columns = (availableWidth / (minCardWidth + 8.dp)).toInt().coerceAtLeast(2)
                val rows = ceil(selectedCategoryProducts.size.toFloat() / columns).toInt()
                val cardImageHeight = (availableWidth / columns) - 8.dp
                val cardTextHeight = 110.dp
                val cardHeight = cardImageHeight + cardTextHeight
                val gridHeight = (cardHeight * rows) + (8.dp * (rows - 1))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = minCardWidth),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(gridHeight),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(selectedCategoryProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { navController.navigate("productScreen/${product.id}") }
                        )
                    }
                }
            }
        }
    }
}