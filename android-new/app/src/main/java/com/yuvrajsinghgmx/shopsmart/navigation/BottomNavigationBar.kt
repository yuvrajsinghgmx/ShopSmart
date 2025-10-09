package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        //BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Saved,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .height(100.dp)
            .drawBehind {
                drawLine(
                    color = colorScheme.outline.copy(alpha = 0.3f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f
                )
            }
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconColor =
                if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
            val textColor =
                if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = iconColor
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = textColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    unselectedIconColor = colorScheme.onSurfaceVariant,
                    selectedTextColor = colorScheme.primary,
                    unselectedTextColor = colorScheme.onSurfaceVariant,
                    indicatorColor = colorScheme.surfaceVariant
                )
            )
        }
    }
}
