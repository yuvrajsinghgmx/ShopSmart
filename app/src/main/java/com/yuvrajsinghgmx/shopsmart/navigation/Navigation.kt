package com.yuvrajsinghgmx.shopsmart.navigation

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yuvrajsinghgmx.shopsmart.screens.*
import com.yuvrajsinghgmx.shopsmart.viewmodel.ShoppingListViewModel

@Composable
fun Navigation(viewModel: ShoppingListViewModel, navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val context = LocalContext.current
    // Obtain SharedPreferences instance
    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    // Add all settings screens to bottom bar visible screens
    val showBottomBar = currentDestination in listOf(
        "Home", "List", "UpComing", "Profile", "MyOrders", "Help",
        "settings", "personal_info", "address_book", "payment_methods", "security",
        "language", "theme", "notifications", "privacy", "currency",
        "shipping_preferences", "order_notifications", "app_version",
        "terms", "privacy_policy", "contact", "faq",
        // New Personalization Settings routes
        "recommendations", "search_history", "size_preferences", "brand_favorites",
        // New Support & Help routes
        "chat_support", "report_issue", "feedback",
        // New Payment & Billing routes
        "saved_cards", "digital_wallet", "billing_history", "refund_settings"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ShopSmartNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "signUpScreen",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Existing routes
            composable("signUpScreen") {
                SignUpScreen(
                    onSignUpComplete = {
                        navController.navigate("Home") {
                            popUpTo("signUpScreen") { inclusive = true }
                        }
                    },
                    onContinueWithEmail = {
                        navController.navigate("emailSignUpScreen")
                    },
                    onTermsAndConditionsClick = {
                        navController.navigate("TermsAndConditions")
                    }
                )
            }

            composable("TermsAndConditions") {
                TermsAndConditionsScreen(
                    onBackClick = {
                        navController.navigate("signUpScreen") {
                            popUpTo("signUpScreen") { inclusive = true }
                        }
                    }
                )
            }

            composable("emailSignUpScreen") {
                EmailSignUpScreen(
                    onSignUpComplete = {
                        navController.navigate("Home") {
                            popUpTo("signUpScreen") { inclusive = true }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate("signUpScreen") {
                            popUpTo("signUpScreen") { inclusive = true }
                        }
                    },
                    onTermsOfUseClicked = {
                        navController.navigate("TermsAndConditions") {
                            popUpTo("signUpScreen") { inclusive = true }
                        }
                    }
                )
            }

            composable("Home") {
                HomeScreen(navController = navController)
            }

            composable("List") {
                ListScreen(viewModel = viewModel, navController = navController)
            }

            composable("UpComing") {
                Upcoming(modifier = Modifier.padding(innerPadding))
            }

            composable("Profile") {
                Profile(navController = navController)
            }

            composable("MyOrders?selectedItems={selectedItems}") { backStackEntry ->
                val selectedItemsJson = backStackEntry.arguments?.getString("selectedItems")
                MyOrders(navController = navController, selectedItemsJson = selectedItemsJson ?: "[]")
            }

            composable("Help") {
                HelpS(navController = navController)
            }

            // Settings Routes
            composable("settings") {
                SettingsScreen(navController = navController)
            }

            // Account Settings
            composable("personal_info") {
                PersonalInformationScreen(navController = navController, context = context, sharedPreferences = sharedPreferences)
            }

            composable("address_book") {
                AddressBookScreen(navController = navController, sharedPreferences = sharedPreferences)
            }

            composable("payment_methods") {
                PaymentMethodsScreen(navController = navController)
            }

            composable("security") {
                SecurityScreen<Any>(navController = navController)
            }

            // App Settings
            composable("language") {
                LanguageScreen(navController = navController)
            }

            composable("theme") {
                ThemeScreen(navController = navController)
            }

            composable("notifications") {
                NotificationSettings(navController = navController)
            }

            composable("privacy") {
                PrivacySettingsScreen(navController = navController)
            }

            // Shopping Settings
            composable("currency") {
                CurrencyScreen(navController = navController)
            }

            composable("shipping_preferences") {
                ShippingPreferencesScreen(navController = navController)
            }

            composable("order_notifications") {
                OrderNotificationsScreen(navController = navController)
            }

            // Personalization Settings Routes
            composable("recommendations") {
                RecommendationsScreen(navController= navController)
            }

            composable("search_history") {
                SearchHistoryScreen(navController= navController)
            }

            composable("size_preferences") {
                SizePreferencesScreen(navController= navController)
            }

            composable("brand_favorites") {
                BrandFavoritesScreen(navController= navController)
            }

            // Support & Help Routes
            composable("faq") {
                FAQScreen(navController = navController)
            }

            composable("chat_support") {
                ComingSoonScreen("Chat Support", navController)
            }

            composable("report_issue") {
                ComingSoonScreen("Report an Issue", navController)
            }

            composable("feedback") {
                ComingSoonScreen("Feedback Center", navController)
            }

            // Payment & Billing Routes
            composable("saved_cards") {
                ComingSoonScreen("Saved Cards", navController)
            }

            composable("digital_wallet") {
                ComingSoonScreen("Digital Wallet", navController)
            }

            composable("billing_history") {
                ComingSoonScreen("Billing History", navController)
            }

            composable("refund_settings") {
                ComingSoonScreen("Refund Settings", navController)
            }

            // About Section
            composable("app_version") {
                AppVersionScreen(navController = navController)
            }

            composable("terms") {
                TermsScreen(navController = navController)
            }

            composable("privacy_policy") {
                PrivacyPolicyScreen(navController = navController)
            }

            composable("contact") {
                ContactScreen(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingSoonScreen(title: String, navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        title,
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "To be implemented soon",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF637478),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This feature is currently under development",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF637478)
            )
        }
    }
}