package com.yuvrajsinghgmx.shopsmart.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel

@Composable
fun UserProfileScreen(
    viewModel: SharedAppViewModel,
    navController: NavController
) {
    val user by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    LaunchedEffect(Unit) {
        viewModel.userState.collect {
            Log.d("UserProfileScreen", "userState emitted: $it")
        }
    }

    when (user?.userType) {
        "CUSTOMER" -> {
            CustomerProfileScreen(viewModel, navController)
        }
        "SHOP_OWNER" -> {
            ShopOwnerProfileScreen(viewModel, navController)
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
