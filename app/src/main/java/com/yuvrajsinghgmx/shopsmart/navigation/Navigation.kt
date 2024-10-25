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
        Screen.Home.route,
        Screen.List.route,
        Screen.UpComing.route,
        Screen.Profile.route,
        Screen.MyOrders().route,
        Screen.Help.route
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
            startDestination = Screen.SignUp.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onContinueWithEmail = {
                        navController.navigate(Screen.EmailSignUp.route)
                    },
                    onTermsAndConditionsClick = {
                        navController.navigate(Screen.TermsAndConditions.route)
                    }
                )
            }

            composable(Screen.TermsAndConditions.route) {
                TermsAndConditionsScreen(
                    onBackClick = {
                        navController.navigate(Screen.SignUp.route){
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.EmailSignUp.route) {
                EmailSignUpScreen(
                    onSignUpComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Screen.SignUp.route){
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onTermsOfUseClicked = {
                        navController.navigate(Screen.TermsAndConditions.route){
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.List.route) {
                ListScreen(viewModel = viewModel, navController = navController)
            }

            composable(Screen.UpComing.route) {
                Upcoming(modifier = Modifier.padding(innerPadding))
            }

            composable(Screen.Profile.route) {
                Profile(navController = navController)
            }

            composable(Screen.MyOrders().route) { backStackEntry ->
                val selectedItemsJson = backStackEntry.arguments?.getString("selectedItems")
                MyOrders(navController = navController, selectedItemsJson = selectedItemsJson ?: "[]")
            }

            composable(Screen.Help.route) {
                HelpS(navController = navController)
            }
        }
    }
}
