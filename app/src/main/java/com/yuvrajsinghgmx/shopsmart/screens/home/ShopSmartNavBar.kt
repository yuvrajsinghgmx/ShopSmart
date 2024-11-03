package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yuvrajsinghgmx.shopsmart.navigation.Screen

data class ButtonNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun ShopSmartNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        ButtonNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        ButtonNavigationItem(
            title = "List",
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.Menu
        ),
        ButtonNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Default.Favorite,
            unselectedIcon = Icons.Default.FavoriteBorder
        ),
        ButtonNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    NavigationBar(tonalElevation = 0.dp, containerColor = Color.Transparent,windowInsets = WindowInsets.navigationBars
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.title
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.popBackStack(route = Screen.Home.routes, inclusive = false)
                        navController.navigate(item.title) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
//                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = Color.Unspecified,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShopSmartNavBarPreview() {
    ShopSmartNavBar(navController = NavHostController(LocalContext.current))
}