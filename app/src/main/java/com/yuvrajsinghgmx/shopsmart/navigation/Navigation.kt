package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yuvrajsinghgmx.shopsmart.screens.HelpS
import com.yuvrajsinghgmx.shopsmart.screens.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.Profile
import com.yuvrajsinghgmx.shopsmart.screens.ShopSmartNavBar
import com.yuvrajsinghgmx.shopsmart.screens.Cart
import com.yuvrajsinghgmx.shopsmart.screens.MyOrders
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Navigation(navController: NavHostController) {
    val viewModel: ShoppingListViewModel = hiltViewModel()
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
            composable("Cart") {
                //val viewModel: ShoppingListViewModel = hiltViewModel() // Assuming you're using Hilt for ViewModel injection
                Cart(viewModel= viewModel,navController=navController)
            }
            composable("Profile") {
                Profile(navController = navController)
            }
            composable("MyOrders?selectedItems={selectedItems}") { backStackEntry ->
                val selectedItemsJson = backStackEntry.arguments?.getString("selectedItems")
                MyOrders(navController = navController, selectedItemsJson = selectedItemsJson ?: "[]")
            }
            composable("Help") {
                HelpS(navController = navController)
            }
        }
    }
}