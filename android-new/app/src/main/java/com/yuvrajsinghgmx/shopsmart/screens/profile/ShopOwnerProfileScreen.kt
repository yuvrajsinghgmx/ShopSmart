package com.yuvrajsinghgmx.shopsmart.screens.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.conn.LoggingSessionOutputBuffer
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel

@Composable
fun ShopOwnerProfileScreen(
    viewModel: SharedAppViewModel,
    navController: NavController
) {
    Text("Shop Owner Profile Screen",
        fontSize = 24.sp)
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login_route") {
                popUpTo("main_graph") { inclusive = true }
                launchSingleTop = true
            }
        }
    ) {
        Text("Logout")
    }
}