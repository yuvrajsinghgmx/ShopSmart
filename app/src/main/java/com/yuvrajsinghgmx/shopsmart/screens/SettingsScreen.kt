package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBackgroundColor
                )
            )
        },
        containerColor = lightBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .weight(1f)
            ) {
                // Account Settings Section
                SettingsSection(title = "Account Settings") {
                    SettingsItem(title = "Personal Information", icon = R.drawable.profile) {
                        navController.navigate("personal_info")
                    }
                    SettingsItem(title = "Address Book", icon = R.drawable.library_books_24px) {
                        navController.navigate("address_book")
                    }
                    SettingsItem(title = "Payment Methods", icon = R.drawable.payments_24px) {
                        navController.navigate("payment_methods")
                    }
                    SettingsItem(title = "Password & Security", icon = R.drawable.lock_24px) {
                        navController.navigate("security")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App Settings Section
                SettingsSection(title = "App Settings") {
                    SettingsItem(title = "Language", icon = R.drawable.language_24px) {
                        navController.navigate("language")
                    }
                    SettingsItem(title = "Theme", icon = R.drawable.contrast_24px) {
                        navController.navigate("theme")
                    }
                    SettingsItem(title = "Notifications", icon = R.drawable.bell) {
                        navController.navigate("notifications")
                    }
                    SettingsItem(title = "Privacy Settings", icon = R.drawable.policy_24px) {
                        navController.navigate("privacy")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Shopping Settings Section
                SettingsSection(title = "Shopping Settings") {
                    SettingsItem(title = "Currency", icon = R.drawable.attach_money_24px) {
                        navController.navigate("currency")
                    }
                    SettingsItem(title = "Shipping Preferences", icon = R.drawable.local_shipping_24px) {
                        navController.navigate("shipping_preferences")
                    }
                    SettingsItem(title = "Order Notifications", icon = R.drawable.orders_24px) {
                        navController.navigate("order_notifications")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // About Section
                SettingsSection(title = "About") {
                    SettingsItem(title = "App Version", icon = R.drawable.conversion_path_24px) {
                        navController.navigate("app_version")
                    }
                    SettingsItem(title = "Terms & Conditions", icon = R.drawable.gavel_24px) {
                        navController.navigate("terms")
                    }
                    SettingsItem(title = "Privacy Policy", icon = R.drawable.admin_panel_settings_24px) {
                        navController.navigate("privacy_policy")
                    }
                    SettingsItem(title = "Contact Us", icon = R.drawable.contact_support_24px) {
                        navController.navigate("contact")
                    }
                }

                // Add some bottom padding to ensure last item is visible
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF233b41),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    icon: Int,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFF637478))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF637478)
            )
        }
    }
}