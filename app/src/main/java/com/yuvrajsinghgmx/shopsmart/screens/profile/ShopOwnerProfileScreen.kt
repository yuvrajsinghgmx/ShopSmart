package com.yuvrajsinghgmx.shopsmart.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel

@Composable
fun ShopOwnerProfileScreen(
    viewModel: SharedAppViewModel,
    navController: NavController
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Shop Owner Profile Screen",
            fontSize = 24.sp
        )
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("login_route") {
                    popUpTo("main_graph") { inclusive = true }
                    launchSingleTop = true
                }
            }
        ) {
            Text("Logout")
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        MenuItem(
            icon = Icons.Outlined.Add,
            text = "Add Shop (temp)",
            onClick = { navController.navigate("addshop") })

    }
}