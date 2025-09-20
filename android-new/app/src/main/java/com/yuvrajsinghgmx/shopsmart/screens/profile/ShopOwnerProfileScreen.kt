package com.yuvrajsinghgmx.shopsmart.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel

@Composable
fun ShopOwnerProfileScreen(
    viewModel: SharedAppViewModel,
    navController: NavController
) {
    Text("Shop Owner Profile Screen",
        fontSize = 24.sp)
}