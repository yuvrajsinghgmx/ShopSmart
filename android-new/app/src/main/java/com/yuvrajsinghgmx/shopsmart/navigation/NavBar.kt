package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.UserProfileScreen

@Composable
fun NavHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(BottomNavItem.Home.route) { HomeScreen(navController=navController) }
        composable(BottomNavItem.Search.route) { SearchScreen() }
        composable(BottomNavItem.Saved.route) { SavedProductScreen() }
        composable(BottomNavItem.Profile.route) { UserProfileScreen() }
    }
}