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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
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
        "Home", "List", "Favorites", "Profile", "MyOrders", "Help", "add_saved_card","google_pay_setup",
        "settings", "personal_info", "address_book", "payment_methods", "security","paypal_settings","apple_pay_setup",
        "language", "theme", "notifications", "privacy", "currency", "coming_soon","spending_analytics",
        "shipping_preferences", "order_notifications", "app_version","transaction_history","view_statements",
        "terms", "privacy_policy", "contact", "faq" , "refund_history", "refund_policy", "contact_support","add_digital_wallet"
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

            composable("Favorites") {
                ComingSoonScreen("Favorites",navController)
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

            // New Settings Routes
            // Main Settings
            composable("settings") {
                SettingsScreen(navController = navController)
            }

            // Account Settings
            composable("personal_info") {
                PersonalInformationScreen(navController = navController, context = context, sharedPreferences = sharedPreferences)
            }

            composable("address_book") {
                AddressBookScreen(navController = navController, sharedPreferences = sharedPreferences) // Pass SharedPreferences here
            }

            composable("payment_methods") {
                PaymentMethodsScreen(navController = navController)
            }

            composable("transaction_history") {
                TransactionHistoryScreen(navController = navController)
            }

            composable("view_statements") {
                ViewStatementsScreen(navController = navController)
            }

            composable("spending_analytics") {
                SpendingAnalyticsScreen(navController = navController)
            }

            composable("add_saved_card") {
                AddSavedCardScreen(navController = navController)
            }

            composable("google_pay_setup") {
                GooglePaySetupScreen(navController = navController)
            }

            composable("paypal_settings") {
                PayPalSetupScreen(navController = navController)
            }

            composable("apple_pay_setup") {
                ApplePaySetupScreen(navController = navController)
            }

            composable("add_digital_wallet") {
                AddDigitalWalletScreen(navController = navController)
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
                ChatSupportScreen(navController =  navController)
            }

            composable("report_issue") {
                ReportAnIssueScreen(
                    onBackPressed = { navController.popBackStack() },
                    onSubmitIssue = { subject, description ->
                        // Handle additional submission logic if needed
                    }
                )
            }

            composable("feedback") {
                FeedbackScreen(
                    onBackPressed = { navController.popBackStack() },
                    onSubmitFeedback = { subject, description ->
                        // Handle feedback submission logic here
                    }
                )
            }

            //coming soon screen
            composable("coming_soon") {
                ComingSoonScreen(title = "Coming Soon", navController = navController)
            }

            // Payment & Billing Routes
            composable("saved_cards") {
                SavedCardsScreen(navController = navController)
            }

            composable("digital_wallet") {
                DigitalWalletScreen(navController = navController)
            }

            composable("billing_history") {
                BillingHistoryScreen(navController = navController)
            }

            composable("refund_settings") {
                RefundSettingsScreen(navController = navController)
            }

            composable("refund_history") {
                RefundHistoryScreen(navController = navController)
            }
            composable("refund_policy") {
                RefundPolicyScreen(navController = navController)
            }

            composable("contact_support") {
                ContactSupportScreen(navController = navController)
            }

            composable("live_chat") {
                LiveChatScreen(navController = navController)
            }

            composable("email_support") {
                EmailSupportScreen(navController = navController)
            }

            composable("phone_support") {
                CallSupportScreen(navController = navController)
            }

            composable("refund_status") {
                TrackRefundScreen(navController = navController)
            }

            composable("return_guidelines") {
                ReturnGuidelinesScreen(navController = navController)
            }

            composable("missing_refund") {
                MissingRefundScreen(navController = navController)
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
            composable("faq") {
                FAQScreen(navController = navController)
            }

            composable("productDetails/{itemsIndex}", arguments = listOf(navArgument("itemsIndex"){
                type = NavType.IntType
            })){
                val index = it.arguments?.getInt("itemsIndex")?:1
                ProductDetails(index = index, navController = navController)
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