package com.yuvrajsinghgmx.shopsmart.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Saved,
        BottomNavItem.Profile
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .drawBehind {
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f
                )
            }
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            val iconColor = if (item == BottomNavItem.Home && isSelected) GreenPrimary else Color(0xFF4D4D4D)
            val textColor = if (item == BottomNavItem.Home && isSelected) GreenPrimary else Color(0xFF4D4D4D)

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
                    indicatorColor = Color.Transparent,
                    selectedIconColor = iconColor,
                    unselectedIconColor = Color(0xFF4D4D4D),
                    selectedTextColor = textColor,
                    unselectedTextColor = Color(0xFF4D4D4D)
                )
            )
        }
    }
}