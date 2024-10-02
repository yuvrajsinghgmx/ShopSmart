package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yuvrajsinghgmx.shopsmart.screens.HomeScreen
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ShoppingListViewModel()
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                HomeScreen(viewModel)
            }
        }
    }
}
