package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Default.Home
        ),
        ButtonNavigationItem(
            title = "List",
            selectedIcon = Icons.Default.List,
            unselectedIcon = Icons.Default.List
        ),
        ButtonNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Default.FavoriteBorder,
            unselectedIcon = Icons.Default.FavoriteBorder
        ),
        ButtonNavigationItem(
            title = "UpComing",
            selectedIcon = Icons.Default.DateRange,
            unselectedIcon = Icons.Default.DateRange
        ),
        ButtonNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Default.Person
        )
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar(tonalElevation = 4.dp) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.title,
                onClick = {
                        navController.navigate(item.title) {
                            launchSingleTop = true
                            restoreState = true
                        }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = Color.Unspecified,
                    indicatorColor = MaterialTheme.colorScheme.surfaceTint
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