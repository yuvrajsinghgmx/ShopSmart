
package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.navigation.AppNavHost
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavItem
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavigationBar
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val bottomNavRoutes = listOf(
                    BottomNavItem.Home.route,
                    BottomNavItem.Cart.route,
                    BottomNavItem.Saved.route,
                    BottomNavItem.Profile.route
                )

                Scaffold(
                    bottomBar = {
                        if (currentRoute in bottomNavRoutes) {
                            BottomNavigationBar(navController = navController)
                        }
                    },
                    content = { padding ->
                        AppNavHost(navController,padding)
                    }
                )
            }
        }
    }
}
