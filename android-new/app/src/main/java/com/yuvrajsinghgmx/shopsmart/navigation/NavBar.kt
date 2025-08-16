package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.savedProducts.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.UserProfileScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.ShopDetail
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.UserProfileViewModel

@Composable
fun NavHost(navController: NavHostController, padding: PaddingValues) {
    val sharedViewModel: SharedShopViewModel = viewModel()

    val userProfileViewModel: UserProfileViewModel = hiltViewModel()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        androidx.navigation.compose.composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        androidx.navigation.compose.composable(BottomNavItem.Search.route) { SearchScreen(onShopClick = { shop ->
            sharedViewModel.setSelectedShop(shop)
            navController.navigate("shopDetails")
        }) }
        androidx.navigation.compose.composable(BottomNavItem.Saved.route) { SavedProductScreen(navController=navController) }
        androidx.navigation.compose.composable(BottomNavItem.Profile.route) {
            UserProfileScreen(
                user = userProfileViewModel.getUserData()
            )
        }
        androidx.navigation.compose.composable("shopDetails") {
            ShopDetail(sharedViewModel = sharedViewModel)
        }
    }
}