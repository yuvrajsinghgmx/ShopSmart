package com.yuvrajsinghgmx.shopsmart.navigation

import UserProfileScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yuvrajsinghgmx.shopsmart.screens.shops.AddShopScreen
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.auth.LoginScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedProductViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.ShopDetail
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.OnBoardingScreen
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.UserRole
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.ProductDetails
import com.yuvrajsinghgmx.shopsmart.screens.savedProducts.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs

@Composable
fun AppNavHost(
    navController: NavHostController,
    padding: PaddingValues
) {
    val sharedProductViewModel: SharedProductViewModel = viewModel()
    val sharedViewModel: SharedShopViewModel = viewModel()
    val sharedAppViewModel: SharedAppViewModel = hiltViewModel()
    val authPrefs: AuthPrefs = sharedAppViewModel.authPrefs

    val startDestination = if (authPrefs.getAccessToken().isNullOrBlank()) {
        "login_route"
    } else {
        "main_graph"
    }

    fun NavGraphBuilder.authGraph(
        navController: NavHostController,
        sharedAppViewModel: SharedAppViewModel
    ) {
        composable("login_route") {
            LoginScreen(
                onLogInSuccess = { isNewUser ->
                    if (isNewUser) {
                        navController.navigate("onboarding") {
                            popUpTo("login_route") { inclusive = true }
                        }
                    } else {
                        navController.navigate("main_graph") {
                            popUpTo("login_route") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier,
                viewModel = sharedAppViewModel,
                authPrefs = sharedAppViewModel.authPrefs,
            )
        }

        composable("onboarding") {

            OnBoardingScreen(
                onboardingViewmodel = sharedAppViewModel,
                onboardingComplete = { role ->
                    when (role) {
                        UserRole.CUSTOMER -> {
                            navController.navigate("main_graph") {
                                popUpTo("login_route") { inclusive = true }
                            }
                        }
                        UserRole.SHOP_OWNER -> {
                            navController.navigate("addshop") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                }

            )
        }

        composable("addshop") {
            AddShopScreen (
                navController = navController,
                onShopAdded = {
                    navController.navigate("main_graph") {
                        popUpTo("login_route") { inclusive = true }
                    }
                }
            )
        }
    }
    fun NavGraphBuilder.mainGraph(
        navController: NavController,
        sharedAppViewModel: SharedAppViewModel,
        sharedViewModel: SharedShopViewModel,
        sharedProductViewModel: SharedProductViewModel
    ) {
        navigation(startDestination = BottomNavItem.Home.route, route = "main_graph") {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = navController, sharedViewModel = sharedViewModel, sharedProductViewModel = sharedProductViewModel)
            }
            composable(BottomNavItem.Search.route) {
                SearchScreen(onShopClick = { shop ->
                    sharedViewModel.setSelectedShop(shop)
                    navController.navigate("shopDetails")
                })
            }
            composable(BottomNavItem.Saved.route) {
                SavedProductScreen(navController = navController)
            }
            composable(BottomNavItem.Profile.route) {
                UserProfileScreen(
                    user = sharedAppViewModel.getUserData(),
                    viewModel = sharedAppViewModel,
                    navController = navController
                )
            }
            composable("shopDetails") {
                ShopDetail(sharedViewModel = sharedViewModel)
            }
            composable("productScreen"){
                ProductDetails(sharedProductViewModel,navController)
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(padding)
    ) {
        authGraph(navController, sharedAppViewModel)
        mainGraph(navController, sharedAppViewModel, sharedViewModel,sharedProductViewModel)
    }
}
