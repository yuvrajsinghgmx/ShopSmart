package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavigationBar
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavItem
import com.yuvrajsinghgmx.shopsmart.screens.SplashScreen
import com.yuvrajsinghgmx.shopsmart.screens.home.HomeScreen
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShopSmartAppContent() // Renamed to avoid clash
                }
            }
        }
    }
}

@Composable
fun ShopSmartAppContent() {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }

    // Auto-hide splash screen after delay (e.g., 2 seconds)
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen(
            onSplashComplete = {
                showSplash = true
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(BottomNavItem.Home.route) {
                        HomeScreen(navController = navController)
                    }
                    // Add more composable screens here
                }
            }
        )
    }
}


