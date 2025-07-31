
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    },
                    content = { padding ->
                        NavHost(navController,padding)
                    }
                )
            }
        }
    }
}
