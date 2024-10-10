package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.HelpS
import com.yuvrajsinghgmx.shopsmart.screens.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.Profile
import com.yuvrajsinghgmx.shopsmart.screens.ShopSmartNavBar
import com.yuvrajsinghgmx.shopsmart.screens.Upcoming
import com.yuvrajsinghgmx.shopsmart.screens.MyOrders
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Navigation(viewModel: ShoppingListViewModel, navController: NavHostController) {
    Scaffold(
        bottomBar = { ShopSmartNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Home") {
                HomeScreen(viewModel = viewModel, navController = navController)
            }
            composable("UpComing") {
                Upcoming(
                    modifier = Modifier.padding(innerPadding),)
            }
            composable("Profile") {
                Profile(navController = navController)
            }
            composable("MyOrders") {
                MyOrders(navController = navController)
            }
            composable("Help") {
                HelpS(navController = navController)
            }
        }
    }
}