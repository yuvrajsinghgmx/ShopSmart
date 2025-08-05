package com.yuvrajsinghgmx.shopsmart.screens.savedProducts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun SavedProductScreen(
    viewModel: SavedViewModel = hiltViewModel(),
    navController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Products", "Shops")

    val savedProducts by viewModel.savedProducts.collectAsState()
    val savedShops by viewModel.savedShops.collectAsState()

    val listState = rememberLazyGridState()
    LaunchedEffect(selectedTab) {
        listState.animateScrollToItem(0)
    }
    Column {
        TopBar(onBack = {navController.popBackStack()}, onSearch = {})
        Tabs(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
        val isProductsTab = selectedTab == 0
        val items = if (isProductsTab) savedProducts else savedShops
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isProductsTab) "No saved products" else "No saved shops",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                if (isProductsTab) {
                    items(savedProducts) { product ->
                        SavedProductCard(product)
                    }
                } else {
                    items(savedShops) { shop ->
                        SavedShopCard(shop)
                    }
                }
            }
        }
    }
}
@Composable
fun TopBar(onBack: () -> Unit, onSearch: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back"
            )
        }
        Text(
            text = "Saved",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onSearch) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}

@Composable
fun Tabs(tabs: List<String>, selectedTab: Int, onTabSelected: (Int) -> Unit ){
    TabRow(
        selectedTabIndex = selectedTab,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab])
                    .height(3.dp),
                color = GreenPrimary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTab == index) GreenPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}