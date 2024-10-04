package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.Profile
import com.yuvrajsinghgmx.shopsmart.screens.ShopSmartNavBar
import com.yuvrajsinghgmx.shopsmart.screens.Upcoming
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Navigation(navController: NavHostController, viewModel: ShoppingListViewModel) {
    val lightBackgroundColor = Color(0xFFF6F5F3)
    Scaffold(
        bottomBar = { ShopSmartNavBar(navController) },
        containerColor = lightBackgroundColor
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Home") { HomeScreen(viewModel, navController) }
            composable("UpComing") { Upcoming() }
            composable("Profile") { Profile() }
        }
    }
}