package com.yuvrajsinghgmx.shopsmart

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.yuvrajsinghgmx.shopsmart.api.API
import com.yuvrajsinghgmx.shopsmart.navigation.Navigation
import com.yuvrajsinghgmx.shopsmart.profilefeatures.BaseActivity
import com.yuvrajsinghgmx.shopsmart.screens.settings.Theme
import com.yuvrajsinghgmx.shopsmart.screens.settings.ThemeManager
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var ImageApi: API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()



            setContent {
            val currentTheme = ThemeManager.currentTheme.collectAsState()

            ShopSmartTheme(
                darkTheme = when (currentTheme.value) {
                    Theme.LIGHT -> false
                    Theme.DARK -> true
                    Theme.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                val viewModel: ShoppingListViewModel = hiltViewModel()
                val navController = rememberNavController()
                Navigation(viewModel = viewModel, navController = navController)
                val context = LocalContext.current



            }
        }
    }
}