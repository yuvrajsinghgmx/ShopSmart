package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.profilefeatures.BankAccountManager
import com.yuvrajsinghgmx.shopsmart.profilefeatures.SavedCardsManager

data class PaymentMethodInfo(
    val title: String,
    val subtitle: String? = null,
    val icon: Int,
    val route: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(navController: NavController) {
    val savedBankAccounts by remember { mutableStateOf(BankAccountManager.savedAccounts) }
    val savedCards by remember { mutableStateOf(SavedCardsManager.savedCards) }

    val digitalWallets = listOf(
        PaymentMethodInfo("Google Pay", null, R.drawable.google_pay_24px, "google_pay_setup"),
        PaymentMethodInfo("PayPal", "Connected", R.drawable.paypal_24px, "paypal_settings"),
        PaymentMethodInfo("Apple Pay", null, R.drawable.apple_pay_24px, "apple_pay_setup")
    )

    val bankAccounts = savedBankAccounts.map { account ->
        PaymentMethodInfo(
            title = "•••• ${account.accountNumber.takeLast(4)}",
            subtitle = "${account.bankName} - ${account.accountType} Account",
            icon = R.drawable.account_balance_24px,
            route = "bank_account_details/${account.accountNumber}"
        )
    }

    val paymentSettings = listOf(
        PaymentMethodInfo(
            "Payment Security",
            "Manage security settings",
            R.drawable.security_24px,
            "payment_security"
        ),
        PaymentMethodInfo(
            "Auto-Pay",
            "Manage automatic payments",
            R.drawable.payments_24px,
            "auto_pay_settings"
        ),
        PaymentMethodInfo(
            "Payment Limits",
            "Set transaction limits",
            R.drawable.money_24px,
            "payment_limits"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Payment Methods",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
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
                    containerColor = Color(0xFFF6F5F3)
                )
            )
        },
        containerColor = Color(0xFFF6F5F3)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Quick Actions
            item {
                QuickActionsSection(navController)
            }

            // Saved Cards Section
            item {
                PaymentMethodSection(
                    title = "Saved Cards",
                    items = savedCards,
                    onItemClick = { route ->
                        route?.let { navController.navigate(it) }
                    },
                    onAddClick = { navController.navigate("add_saved_card") }  // Changed from "coming_soon" to "add_saved_card"
                )
            }

            // Digital Wallets Section
            item {
                PaymentMethodSection(
                    title = "Digital Wallets",
                    items = digitalWallets,
                    onItemClick = { route ->
                        route?.let { navController.navigate(it) }
                    },
                    onAddClick = { navController.navigate("add_digital_wallet") } // New route for adding digital wallet
                )
            }

            // Bank Accounts Section
            item {
                PaymentMethodSection(
                    title = "Bank Accounts",
                    items = bankAccounts,
                    onItemClick = { route ->
                        route?.let { navController.navigate(it) }
                    },
                    onAddClick = { navController.navigate("add_bank_account") }
                )
            }

            // Payment Settings
            item {
                PaymentMethodSection(
                    title = "Payment Settings",
                    items = paymentSettings,
                    onItemClick = { route ->
                        route?.let { navController.navigate("coming_soon") }
                    },
                    showAddButton = false
                )
            }

            // Support Card
            item {
                SupportCard(navController)
            }
        }
    }
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton(
                icon = R.drawable.history_24px,
                text = "Payment\nHistory",
                onClick = { navController.navigate("transaction_history") }
            )
            QuickActionButton(
                icon = R.drawable.receipt_long_24px,
                text = "View\nStatements",
                onClick = { navController.navigate("view_statements") }
            )
            QuickActionButton(
                icon = R.drawable.analytics_24px,
                text = "Spending\nAnalytics",
                onClick = { navController.navigate("spending_analytics") }
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color(0xFF006D40),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF006D40),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun PaymentMethodSection(
    title: String,
    items: List<PaymentMethodInfo>,
    onItemClick: (String?) -> Unit,
    onAddClick: (() -> Unit)? = null,
    showAddButton: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF332D25)
        )

        if (items.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = Color.White
            ) {
                when (title) {
                    "Saved Cards" -> EmptyCardsMessage(onAddCard = onAddClick ?: {})
                    "Bank Accounts" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.account_balance_24px),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF637478)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No Bank Accounts Added",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Add your bank account for easy transfers",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF637478),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = onAddClick ?: {},
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF006D40)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Bank Account")
                            }
                        }
                    }
                    "Digital Wallets" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.payments_24px),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF637478)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No Digital Wallets Connected",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Connect your preferred digital payment method",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF637478),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = onAddClick ?: {},
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF006D40)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Digital Wallet")
                            }
                        }
                    }
                    else -> {
                        // Default empty state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.payments_24px),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF637478)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No Items Added",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (showAddButton && onAddClick != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedButton(
                                    onClick = onAddClick,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF006D40)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add ${title.dropLast(1)}")
                                }
                            }
                        }
                    }
                }
            }
        } else {
            items.forEach { item ->
                PaymentMethodItem(item = item, onClick = { onItemClick(item.route) })
            }

            if (showAddButton && onAddClick != null) {
                OutlinedButton(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF006D40)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add ${title.dropLast(1)}")
                }
            }
        }
    }
}

@Composable
private fun EmptyCardsMessage(onAddCard: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.credit_card_24px),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFF637478)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No saved cards",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onAddCard,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF006D40)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Card")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodItem(
    item: PaymentMethodInfo,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF637478)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                item.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_24px),
                contentDescription = "Navigate",
                tint = Color(0xFF637478)
            )
        }
    }
}

@Composable
private fun SupportCard(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFFFF4ED)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.help_24px),
                contentDescription = null,
                tint = Color(0xFFB25E02)
            )
            Column {
                Text(
                    "Need Help?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB25E02)
                )
                Text(
                    "Contact support for payment-related issues",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB25E02)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { navController.navigate("contact_support") },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFB25E02)
                    )
                ) {
                    Text("Contact Support")
                }
            }
        }
    }
}
