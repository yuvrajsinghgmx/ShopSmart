package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.navigation.BottomNavigationBar
import com.yuvrajsinghgmx.shopsmart.navigation.NavHost
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("This is ShopSmart App.")
                }
            }
            val navController = rememberNavController()

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

