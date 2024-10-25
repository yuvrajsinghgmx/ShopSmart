package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yuvrajsinghgmx.shopsmart.screens.*
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Navigation(viewModel: ShoppingListViewModel, navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val showBottomBar = currentDestination in listOf(
        Screen.Home.routes,
        Screen.List.routes,
        Screen.UpComing.routes,
        Screen.Profile.routes,
        Screen.MyOrders().routes,
        Screen.Help.routes
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ShopSmartNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.SignUp.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.SignUp.routes) {
                SignUpScreen(
                    onSignUpComplete = {
                        navController.navigate(Screen.Home.routes) {
                            popUpTo(Screen.SignUp.routes) { inclusive = true }
                        }
                    },
                    onContinueWithEmail = {
                        navController.navigate(Screen.EmailSignUp.routes)
                    },
                    onTermsAndConditionsClick = {
                        navController.navigate(Screen.TermsAndConditions.routes)
                    }
                )
            }

            composable(Screen.TermsAndConditions.routes) {
                TermsAndConditionsScreen(
                    onBackClick = {
                        navController.navigate(Screen.SignUp.routes){
                            popUpTo(Screen.SignUp.routes) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.EmailSignUp.routes) {
                EmailSignUpScreen(
                    onSignUpComplete = {
                        navController.navigate(Screen.Home.routes) {
                            popUpTo(Screen.SignUp.routes) { inclusive = true }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Screen.SignUp.routes){
                            popUpTo(Screen.SignUp.routes) { inclusive = true }
                        }
                    },
                    onTermsOfUseClicked = {
                        navController.navigate(Screen.TermsAndConditions.routes){
                            popUpTo(Screen.SignUp.routes) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.routes) {
                HomeScreen(navController = navController)
            }

            composable(Screen.List.routes) {
                ListScreen(viewModel = viewModel, navController = navController)
            }

            composable(Screen.UpComing.routes) {
                Upcoming(modifier = Modifier.padding(innerPadding))
            }

            composable(Screen.Profile.routes) {
                Profile(navController = navController)
            }

            composable(Screen.MyOrders().routes) { backStackEntry ->
                val selectedItemsJson = backStackEntry.arguments?.getString("selectedItems")
                MyOrders(navController = navController, selectedItemsJson = selectedItemsJson ?: "[]")
            }

            composable(Screen.Help.routes) {
                HelpS(navController = navController)
            }
        }
    }
}
