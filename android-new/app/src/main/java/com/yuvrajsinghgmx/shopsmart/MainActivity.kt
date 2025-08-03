package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.data.preferences.UserPreferences
import com.yuvrajsinghgmx.shopsmart.navigation.AppNavHost
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavigationBar
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavItem
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                val context = LocalContext.current
                val prefs = remember { UserPreferences(context) }
                val onboardingDone by prefs.onboardingCompleted.collectAsState(initial = false)

                // Determine start destination based on onboarding completion
                val startDest = if (onboardingDone) BottomNavItem.Home.route
                               else "onboarding_user_type"

                val navController = rememberNavController()

                // Observe the current back-stack entry so we can react to route changes
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Show bottom bar only when **not** on any onboarding screen
                val showBottomBar = currentRoute?.startsWith("onboarding_") == false

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        padding = innerPadding,
                        startDestination = startDest
                    )
                }
            }
        }
    }
}
