package com.yuvrajsinghgmx.shopsmart.navigation
import com.yuvrajsinghgmx.shopsmart.screens.shop.addShopScreen

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
import com.yuvrajsinghgmx.shopsmart.screens.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.UserProfileScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.ShopDetail
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.UserProfileViewModel
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.UserTypeSelectionScreen
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.ProfileDetailsScreen

@Composable
fun AppNavHost(
    navController: NavHostController, 
    padding: PaddingValues,
    startDestination: String = "onboarding_user_type"  // ← Add this parameter with default value
) {
    val sharedViewModel: SharedShopViewModel = viewModel()
    val userProfileViewModel: UserProfileViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,  // ← Use the parameter instead of hardcoded value
        modifier = Modifier.padding(padding)
    ) {
        // Onboarding Screens
        composable("onboarding_user_type") {
            UserTypeSelectionScreen(navController = navController)
        }
        
        composable("onboarding_profile/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            ProfileDetailsScreen(navController = navController, userType = userType)
        }

        // Main App Screens
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(BottomNavItem.Search.route) { 
            SearchScreen(onShopClick = { shop ->
                sharedViewModel.setSelectedShop(shop)
                navController.navigate("shopDetails")
            }) 
        }
        composable(BottomNavItem.Saved.route) { SavedProductScreen() }
        composable(BottomNavItem.Profile.route) {
            UserProfileScreen(
                user = userProfileViewModel.getUserData()
            )
        }
        composable("shopDetails") {
            ShopDetail(sharedViewModel = sharedViewModel)
        }
        composable("add_shop") {
            AddShopScreen(navController = navController)
        }
    }
}
