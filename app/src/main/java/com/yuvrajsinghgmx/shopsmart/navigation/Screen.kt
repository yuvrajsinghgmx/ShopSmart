package com.yuvrajsinghgmx.shopsmart.navigation

sealed class Screen(val routes: String) {
    data object SignUp : Screen("signUpScreen")
    data object TermsAndConditions : Screen("TermsAndConditions")
    data object EmailSignUp : Screen("emailSignUpScreen")
    data object Home : Screen("Home")
    data object List : Screen("List")
    data object UpComing : Screen("UpComing")
    data object Profile : Screen("Profile")
    data class MyOrders(val selectedItems: String? = null) : Screen("MyOrders?selectedItems={selectedItems}")
    data object Help : Screen("Help")
}