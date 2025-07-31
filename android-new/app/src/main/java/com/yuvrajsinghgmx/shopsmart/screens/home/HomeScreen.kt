package com.yuvrajsinghgmx.shopsmart.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.ShopCard
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedShopViewModel,
    navController: NavController
) {
    val state = viewModel.state.value
    val categories = listOf("All", "Groceries", "Fashion", "Electronics")


    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // ✅ Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hi, User",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Showing shops within 10 km",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            AsyncImage(
                model = "https://i.pravatar.cc/150?img=3",
                contentDescription = "Profile",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable { },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Search Bar
        OutlinedTextField(
            value = state.searchQuery ?: "",
            onValueChange = { viewModel.onEvent(HomeEvent.Search(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
            },
            placeholder = { Text("Search shops or products...") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5)
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // ✅ Horizontal Scrollable Category Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    label = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Trending Products
        Text(
            text = "Trending Products",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(state.products) { product ->
                ProductCard(product = product)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Nearby Shops
        Text(
            text = "Nearby Shops",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.nearbyShops) { shop ->
                ShopCard(shop = shop, onClick = {
                    sharedViewModel.setSelectedShop(shop)
                    navController.navigate("shopDetails")
                })
            }
        }
    }
}

// ✅ Reusable Category Chip Component
@Composable
fun CategoryChip(label: String, isSelected: Boolean = false, onClick: () -> Unit) {
    val selectedBg = Color(0xFF00B44B)
    val unselectedBg = Color(0xFFF5F5F5)
    val selectedTextColor = Color.White
    val unselectedTextColor = Color(0xFF666666)


    Surface(
        color = if (isSelected) selectedBg else unselectedBg,
        shape = RoundedCornerShape(50),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = Modifier
            .defaultMinSize(minHeight = 36.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = label,
                color = if (isSelected) selectedTextColor else unselectedTextColor,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}