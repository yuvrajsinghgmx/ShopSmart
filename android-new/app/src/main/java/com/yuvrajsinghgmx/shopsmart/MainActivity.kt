package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavigationBar
import com.yuvrajsinghgmx.shopsmart.navigation.NavHost
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ShopSmartTheme {
                Scaffold (
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }, content = { padding ->
                        NavHost(navController = navController, padding = padding)
                    }
                )
            }
        }
    }
}

