package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (
    val route: String,
    val icon: ImageVector,
    val label: String
){
    object Home: BottomNavItem("home", Icons.Default.Home, "Home")
    object Search: BottomNavItem("search", Icons.Default.Search, "Search")
    object Saved: BottomNavItem("saved", Icons.Default.Favorite, "Saved")
    object Profile: BottomNavItem("profile", Icons.Default.Person, "Profile")
}