package com.yuvrajsinghgmx.shopsmart.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF332D25)
                    )
                }

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF332D25),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
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

                // Personalization Settings Section
                SettingsSection(title = "Personalization Settings") {
                    SettingsItem(title = "Recommendations", icon = R.drawable.recommend_24px) {
                        navController.navigate("recommendations")
                    }
                    SettingsItem(title = "Search History", icon = R.drawable.history_24px) {
                        navController.navigate("search_history")
                    }
                    SettingsItem(title = "Size Preferences", icon = R.drawable.straighten_24px) {
                        navController.navigate("size_preferences")
                    }
                    SettingsItem(title = "Brand Favorites", icon = R.drawable.favorite_24px) {
                        navController.navigate("brand_favorites")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Support & Help Section
                SettingsSection(title = "Support & Help") {
                    SettingsItem(title = "FAQ Center", icon = R.drawable.help_24px) {
                        navController.navigate("faq")
                    }
                    SettingsItem(title = "Chat Support", icon = R.drawable.chat_24px) {
                        navController.navigate("chat_support")
                    }
                    SettingsItem(title = "Report an Issue", icon = R.drawable.report_problem_24px) {
                        navController.navigate("report_issue")
                    }
                    SettingsItem(title = "Feedback Center", icon = R.drawable.feedback_24px) {
                        navController.navigate("feedback")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Payment & Billing Section
                SettingsSection(title = "Payment & Billing") {
                    SettingsItem(title = "Saved Cards", icon = R.drawable.credit_card_24px) {
                        navController.navigate("saved_cards")
                    }
                    SettingsItem(title = "Digital Wallet", icon = R.drawable.account_balance_wallet_24px) {
                        navController.navigate("digital_wallet")
                    }
                    SettingsItem(title = "Billing History", icon = R.drawable.receipt_24px) {
                        navController.navigate("billing_history")
                    }
                    SettingsItem(title = "Refund Settings", icon = R.drawable.money_24px) {
                        navController.navigate("refund_settings")
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
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                color = Color(0xFF637478),
                fontWeight = FontWeight.Normal
            )
        }
    }
}