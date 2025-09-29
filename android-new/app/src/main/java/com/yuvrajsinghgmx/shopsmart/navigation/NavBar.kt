package com.yuvrajsinghgmx.shopsmart.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.screens.SearchScreen
import com.yuvrajsinghgmx.shopsmart.screens.auth.LoginScreen
import com.yuvrajsinghgmx.shopsmart.screens.cart.CartScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedProductViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.SharedShopViewModel
import com.yuvrajsinghgmx.shopsmart.screens.home.ShopDetail
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.OnBoardingScreen
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.UserRole
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.ProductDetails
import com.yuvrajsinghgmx.shopsmart.screens.profile.UserProfileScreen
import com.yuvrajsinghgmx.shopsmart.screens.review.ReviewScreen
import com.yuvrajsinghgmx.shopsmart.screens.savedProducts.SavedProductScreen
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel
import com.yuvrajsinghgmx.shopsmart.screens.shops.AddShopScreen
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs

@Composable
fun AppNavHost(
    navController: NavHostController,
    padding: PaddingValues
) {
    val sharedProductViewModel: SharedProductViewModel = hiltViewModel()
    val sharedViewModel: SharedShopViewModel = viewModel()
    val sharedAppViewModel: SharedAppViewModel = hiltViewModel()
    val authPrefs = AuthPrefs(LocalContext.current)

    val startDestination = when {
        authPrefs.getAccessToken().isNullOrBlank() -> "login_route"
        !authPrefs.isOnboardingCompleted() -> "onboarding"
        else -> "main_graph"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(padding)
    ) {
        authGraph(navController, sharedAppViewModel)
        mainGraph(navController, sharedAppViewModel, sharedViewModel, sharedProductViewModel)
    }
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
                            popUpTo("onboarding") { inclusive = true }
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
        AddShopScreen(
            navController = navController,
            onShopAdded = {
                navController.navigate("main_graph") {
                    popUpTo("addshop") { inclusive = true }
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
            HomeScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                sharedProductViewModel = sharedProductViewModel
            )
        }
/*        composable(BottomNavItem.Search.route) {
            SearchScreen(onShopClick = { shop ->
                sharedViewModel.setSelectedShop(shop)
                navController.navigate("shopDetails")
            })
        }*/
        composable(BottomNavItem.Cart.route) {
            CartScreen(
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
            )
        }
        composable(BottomNavItem.Saved.route) {
            SavedProductScreen(onBack = { handleBack(navController) })
        }
        composable(BottomNavItem.Profile.route) {
            UserProfileScreen(
                viewModel = sharedAppViewModel,
                navController = navController
            )
        }
        composable("shopDetails") {
            ShopDetail(
                sharedViewModel = sharedViewModel,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("productScreen") {
            ProductDetails(sharedProductViewModel, navController)
        }
        composable("reviewScreen/{type}/{id}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val target = when (type) {
                "product" -> ReviewTarget.Product(id)
                "shop" -> ReviewTarget.Shop(id)
                else -> throw IllegalArgumentException("Unknown Review type")
            }
            ReviewScreen(target = target, navController = navController)
        }
    }
}

fun handleBack(navController: NavController, fallbackRoute: String = BottomNavItem.Home.route) {
    if (!navController.navigateUp()) {
        navController.navigate(fallbackRoute) {
            launchSingleTop = true
            restoreState = true
        }
    }
}