package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


private data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@Composable
fun ShopSmartNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "Home"
        ),
        NavigationItem(
            title = "List",
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.Menu,
            route = "List"
        ),
        NavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Default.Favorite,
            unselectedIcon = Icons.Default.FavoriteBorder,
            route = "Favorites"
        ),
        NavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = "Profile"
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .zIndex(1f),
        color = Color.White,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)  // Slightly reduced height
                    .background(Color.White)
                    .clip(RoundedCornerShape(28.dp))
                    .padding(horizontal = 12.dp),  // Slightly reduced horizontal padding
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationItems.forEach { item ->
                    val isSelected = currentDestination == item.route

                    IconButton(
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.size(44.dp)  // Slightly reduced button size
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp),  // Slightly reduced icon size
                            tint = if (isSelected) Color.Black else Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ShopSmartNavBarPreview() {
    MaterialTheme {
        ShopSmartNavBar(navController = NavHostController(LocalContext.current))
    }
}