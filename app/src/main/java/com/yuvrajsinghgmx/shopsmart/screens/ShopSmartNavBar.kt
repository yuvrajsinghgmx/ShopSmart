package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun ShopSmartNavBar(navController: NavHostController) {
    val items = listOf(
        ButtonNavigationItem(
            title = "Home",
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Default.Home
        ),
        ButtonNavigationItem(
            title = "UpComing",
            selectedIcon = Icons.Default.DateRange,
            unselectedIcon = Icons.Default.DateRange
        ),
        ButtonNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Default.Person
        )
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val lightBackgroundColor = Color(0xFFF6F5F3)

    NavigationBar(containerColor = lightBackgroundColor) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.title){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
            )
        }
    }
}