package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.text.NumberFormat
import java.util.*

data class TransactionLimit(
    val type: String,
    var amount: Double,
    var isEnabled: Boolean = true,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentLimitsScreen(navController: NavController) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedLimit by remember { mutableStateOf<TransactionLimit?>(null) }

    var limits by remember {
        mutableStateOf(
            listOf(
                TransactionLimit(
                    "Daily Transaction Limit",
                    1000.0,
                    true,
                    "Maximum amount for all transactions in a day"
                ),
                TransactionLimit(
                    "Single Transaction Limit",
                    500.0,
                    true,
                    "Maximum amount for a single transaction"
                ),
                TransactionLimit(
                    "ATM Withdrawal Limit",
                    300.0,
                    true,
                    "Maximum daily ATM withdrawal amount"
                ),
                TransactionLimit(
                    "Online Purchase Limit",
                    750.0,
                    true,
                    "Maximum amount for online purchases"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Payment Limits",
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview Card
            item {
                TransactionOverviewCard()
            }

            // Quick Actions
            item {
                QuickActionsCard(navController)
            }

            // Transaction Limits
            item {
                Text(
                    "Transaction Limits",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Limits List
            items(limits.size) { index ->
                val limit = limits[index]
                TransactionLimitCard(
                    limit = limit,
                    onEditClick = {
                        selectedLimit = limit
                        showEditDialog = true
                    },
                    onToggle = { isEnabled ->
                        limits = limits.toMutableList().apply {
                            set(index, limit.copy(isEnabled = isEnabled))
                        }
                    }
                )
            }

            // Information Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFFF0F7FF)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.info_24px),
                            contentDescription = null,
                            tint = Color(0xFF0055D4)
                        )
                        Column {
                            Text(
                                "About Transaction Limits",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0055D4)
                            )
                            Text(
                                "Transaction limits help protect your account from unauthorized use and manage your spending. Changes to limits may take up to 24 hours to take effect.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0055D4)
                            )
                        }
                    }
                }
            }
        }

        if (showEditDialog && selectedLimit != null) {
            EditLimitDialog(
                limit = selectedLimit!!,
                onDismiss = {
                    showEditDialog = false
                    selectedLimit = null
                },
                onSave = { updatedLimit ->
                    limits = limits.map { limit ->
                        if (limit.type == updatedLimit.type) updatedLimit
                        else limit
                    }
                    showEditDialog = false
                    selectedLimit = null
                }
            )
        }
    }
}

@Composable
private fun TransactionOverviewCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Today's Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = { /* View details */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF006D40)
                    )
                ) {
                    Text("View Details")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Remaining Daily Limit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        "$850.00",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Spent Today",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        "$150.00",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LinearProgressIndicator(
                progress = { 0.15f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF006D40),
                trackColor = Color(0xFFFFFFFF),
            )
        }
    }
}

@Composable
private fun QuickActionsCard(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton(
                icon = R.drawable.history_24px,
                text = "Transaction\nHistory",
                onClick = { navController.navigate("transaction_history") }
            )
            QuickActionButton(
                icon = R.drawable.lightbulb_24px,
                text = "Spending\nAnalytics",
                onClick = { navController.navigate("spending_analytics") }
            )
            QuickActionButton(
                icon = R.drawable.notifications_24px,
                text = "Limit\nAlerts",
                onClick = { navController.navigate("coming_soon") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionLimitCard(
    limit: TransactionLimit,
    onEditClick: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        onClick = onEditClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        limit.type,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        NumberFormat.getCurrencyInstance(Locale.US).format(limit.amount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF006D40),
                        fontWeight = FontWeight.Bold
                    )
                }
                Switch(
                    checked = limit.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF006D40),
                        checkedTrackColor = Color(0xFFE7F5EC)
                    )
                )
            }
            Text(
                limit.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF637478)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditLimitDialog(
    limit: TransactionLimit,
    onDismiss: () -> Unit,
    onSave: (TransactionLimit) -> Unit
) {
    var amount by remember { mutableStateOf(limit.amount.toString()) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Edit ${limit.type}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    limit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        showError = false
                    },
                    label = { Text("Amount") },
                    prefix = { Text("$") },
                    singleLine = true,
                    isError = showError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError) {
                    Text(
                        "Please enter a valid amount",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newAmount = amount.toDoubleOrNull()
                    if (newAmount != null && newAmount > 0) {
                        onSave(limit.copy(amount = newAmount))
                    } else {
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}