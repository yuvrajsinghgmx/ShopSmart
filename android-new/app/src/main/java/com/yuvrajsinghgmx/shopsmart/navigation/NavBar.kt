package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.UserProfileScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.ShopDetail
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedShopViewModel

@Composable
fun NavHost(navController: NavHostController, padding: PaddingValues) {
    val sharedViewModel: SharedShopViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(BottomNavItem.Search.route) { SearchScreen() }
        composable(BottomNavItem.Saved.route) { SavedProductScreen() }
        composable(BottomNavItem.Profile.route) { UserProfileScreen() }
        composable("shopDetails") {
            ShopDetail(sharedViewModel = sharedViewModel)
        }
    }
}