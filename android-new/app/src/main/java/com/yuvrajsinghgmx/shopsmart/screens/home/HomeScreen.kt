package com.yuvrajsinghgmx.shopsmart.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ShopCard
import com.yuvrajsinghgmx.shopsmart.sharedComponents.SearchBarComposable
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedShopViewModel, // ✅ Passed from NavHost
    navController: NavController
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // ✅ Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                ShopSmartTheme {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hi, User",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Showing shops within 10 Km",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(50.dp),
                tint = GreenPrimary
            )
        }

        // ✅ Loading / Error State
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }
            state.error != null -> {
                Text(text = state.error, modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Search Bar
        SearchBarComposable(
            query = state.searchQuery ?: "",
            onQueryChange = { viewModel.onEvent(HomeEvent.Search(it)) },
            onSearch = { viewModel.onEvent(HomeEvent.Search(state.searchQuery ?: "")) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Trending Products Title
        ShopSmartTheme {
            Text(
                text = "Trending Products",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Trending Products List
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(state.products) { product ->
                ProductCard(product = product)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Nearby Shops Title
        ShopSmartTheme {
            Text(
                text = "Nearby Shops",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Nearby Shops List with Clickable Navigation
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(state.nearbyShops) { shop ->
                ShopCard(shop = shop, onClick = {
                    sharedViewModel.setSelectedShop(shop) // ✅ Save selected shop
                    navController.navigate("shopDetails") // ✅ Navigate to details screen
                })
            }
        }
    }
}