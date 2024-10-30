package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.text.SimpleDateFormat
import java.util.*

data class TransactionItem(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val date: Long,
    val status: TransactionStatus
)

enum class TransactionType {
    CREDIT, DEBIT, REFUND
}

enum class TransactionStatus {
    COMPLETED, PENDING, FAILED
}

data class LinkedAccount(
    val id: String,
    val name: String,
    val type: AccountType,
    val lastFourDigits: String,
    val isDefault: Boolean = false
)

enum class AccountType {
    BANK_ACCOUNT, UPI, CREDIT_CARD, DEBIT_CARD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalWalletScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)
    var showAddAccountDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    // Sample data
    var walletBalance by remember { mutableStateOf(5000.0) }
    var transactions by remember {
        mutableStateOf(
            listOf(
                TransactionItem(
                    "1",
                    TransactionType.CREDIT,
                    2500.0,
                    "Added from bank account",
                    System.currentTimeMillis() - 3600000,
                    TransactionStatus.COMPLETED
                ),
                TransactionItem(
                    "2",
                    TransactionType.DEBIT,
                    1200.0,
                    "Purchase - Order #1234",
                    System.currentTimeMillis() - 86400000,
                    TransactionStatus.COMPLETED
                ),
                TransactionItem(
                    "3",
                    TransactionType.REFUND,
                    800.0,
                    "Refund - Order #1230",
                    System.currentTimeMillis() - 172800000,
                    TransactionStatus.COMPLETED
                )
            )
        )
    }

    var linkedAccounts by remember {
        mutableStateOf(
            listOf(
                LinkedAccount(
                    "1",
                    "HDFC Bank",
                    AccountType.BANK_ACCOUNT,
                    "4567",
                    true
                ),
                LinkedAccount(
                    "2",
                    "Google Pay UPI",
                    AccountType.UPI,
                    "8901"
                ),
                LinkedAccount(
                    "3",
                    "VISA Card",
                    AccountType.CREDIT_CARD,
                    "1234"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Digital Wallet",
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
                actions = {
                    IconButton(onClick = { showAddAccountDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_24px),
                            contentDescription = "Add Account"
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
                .padding(innerPadding)
        ) {
            // Balance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Wallet Balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "₹${String.format("%.2f", walletBalance)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { /* Add Money */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0E8545)
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_24px),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Money")
                        }
                        OutlinedButton(
                            onClick = { /* Send Money */ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.send_24px),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Money")
                        }
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Transactions") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Linked Accounts") }
                )
            }

            when (selectedTab) {
                0 -> TransactionsList(transactions)
                1 -> LinkedAccountsList(
                    accounts = linkedAccounts,
                    onSetDefault = { account ->
                        linkedAccounts = linkedAccounts.map {
                            it.copy(isDefault = it.id == account.id)
                        }
                    },
                    onRemove = { account ->
                        linkedAccounts = linkedAccounts.filter { it.id != account.id }
                    }
                )
            }
        }
    }
}

@Composable
private fun TransactionsList(transactions: List<TransactionItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction)
        }
    }
}

@Composable
private fun TransactionItem(transaction: TransactionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = when (transaction.type) {
                            TransactionType.CREDIT -> R.drawable.arrow_downward_24px
                            TransactionType.DEBIT -> R.drawable.arrow_upward_24px
                            TransactionType.REFUND -> R.drawable.refresh_24px
                        }
                    ),
                    contentDescription = null,
                    tint = when (transaction.type) {
                        TransactionType.CREDIT -> Color(0xFF0E8545)
                        TransactionType.DEBIT -> MaterialTheme.colorScheme.error
                        TransactionType.REFUND -> MaterialTheme.colorScheme.tertiary
                    }
                )
                Column {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                            .format(Date(transaction.date)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = when (transaction.type) {
                        TransactionType.CREDIT -> "+₹${String.format("%.2f", transaction.amount)}"
                        TransactionType.DEBIT -> "-₹${String.format("%.2f", transaction.amount)}"
                        TransactionType.REFUND -> "+₹${String.format("%.2f", transaction.amount)}"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (transaction.type) {
                        TransactionType.CREDIT -> Color(0xFF0E8545)
                        TransactionType.DEBIT -> MaterialTheme.colorScheme.error
                        TransactionType.REFUND -> MaterialTheme.colorScheme.tertiary
                    }
                )
                AssistChip(
                    onClick = { },
                    label = { Text(transaction.status.name) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (transaction.status) {
                            TransactionStatus.COMPLETED -> Color(0xFF0E8545).copy(alpha = 0.1f)
                            TransactionStatus.PENDING -> MaterialTheme.colorScheme.primaryContainer
                            TransactionStatus.FAILED -> MaterialTheme.colorScheme.errorContainer
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun LinkedAccountsList(
    accounts: List<LinkedAccount>,
    onSetDefault: (LinkedAccount) -> Unit,
    onRemove: (LinkedAccount) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(accounts) { account ->
            LinkedAccountItem(
                account = account,
                onSetDefault = { onSetDefault(account) },
                onRemove = { onRemove(account) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LinkedAccountItem(
    account: LinkedAccount,
    onSetDefault: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = when (account.type) {
                            AccountType.BANK_ACCOUNT -> R.drawable.account_balance_24px
                            AccountType.UPI -> R.drawable.payment_24px
                            AccountType.CREDIT_CARD -> R.drawable.credit_card_24px
                            AccountType.DEBIT_CARD -> R.drawable.credit_card_24px
                        }
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "••••${account.lastFourDigits}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Row {
                if (!account.isDefault) {
                    TextButton(onClick = onSetDefault) {
                        Text("Set Default")
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_24px),
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}