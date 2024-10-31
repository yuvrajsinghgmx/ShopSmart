package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PaymentTransaction(
    val id: String,
    val amount: Double,
    val type: TransactionType,
    val status: TransactionStatus,
    val date: LocalDateTime,
    val description: String,
    val paymentMethod: String
)

enum class TransactionType {
    PURCHASE, REFUND, PAYMENT, SUBSCRIPTION
}

enum class TransactionStatus {
    COMPLETED, PENDING, FAILED, PROCESSING
}

data class TransactionFilter(
    val types: Set<TransactionType> = emptySet(),
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val minAmount: Double? = null,
    val maxAmount: Double? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalWalletScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<PaymentTransaction?>(null) }
    var filters by remember { mutableStateOf(TransactionFilter()) }

    // Sample transactions
    val transactions = remember {
        listOf(
            PaymentTransaction(
                "TRX-001",
                299.99,
                TransactionType.PURCHASE,
                TransactionStatus.COMPLETED,
                LocalDateTime.now().minusDays(1),
                "Electronics Purchase",
                "Credit Card ****1234"
            ),
            PaymentTransaction(
                "TRX-002",
                -49.99,
                TransactionType.REFUND,
                TransactionStatus.COMPLETED,
                LocalDateTime.now().minusDays(2),
                "Product Return Refund",
                "Original Payment Method"
            ),
            PaymentTransaction(
                "TRX-003",
                149.99,
                TransactionType.PAYMENT,
                TransactionStatus.PROCESSING,
                LocalDateTime.now().minusDays(3),
                "Online Payment",
                "PayPal"
            ),
            PaymentTransaction(
                "TRX-004",
                9.99,
                TransactionType.SUBSCRIPTION,
                TransactionStatus.PENDING,
                LocalDateTime.now().minusDays(4),
                "Premium Subscription",
                "Google Pay"
            )
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Payment History",
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
                SearchAndFilterBar(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    onFilterClick = { showFilters = !showFilters }
                )
            }
        },
        containerColor = Color(0xFFF6F5F3)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            item {
                TransactionSummaryCard(transactions)
            }

            // Transaction List
            items(transactions.filter {
                it.description.contains(searchQuery, ignoreCase = true) ||
                        it.id.contains(searchQuery, ignoreCase = true)
            }) { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onClick = { selectedTransaction = transaction }
                )
            }
        }
    }

    // Transaction Details Dialog
    selectedTransaction?.let { transaction ->
        TransactionDetailsDialog(
            transaction = transaction,
            onDismiss = { selectedTransaction = null }
        )
    }

    // Filter Sheet
    if (showFilters) {
        FilterBottomSheet(
            currentFilter = filters,
            onFilterChange = { filters = it },
            onDismiss = { showFilters = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAndFilterBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Search transactions") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF637478)
            )
        },
        trailingIcon = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(id = R.drawable.filter_list_24px),
                    contentDescription = "Filter",
                    tint = Color(0xFF637478)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF006D40),
            unfocusedBorderColor = Color(0xFFE5E7EB)
        ),
        singleLine = true
    )
}

@Composable
private fun TransactionSummaryCard(transactions: List<PaymentTransaction>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                title = "Total Spent",
                value = "₹${transactions.filter { it.type == TransactionType.PURCHASE }
                    .sumOf { it.amount }}",
                icon = R.drawable.orders_24px
            )
            SummaryItem(
                title = "Refunds",
                value = "₹${transactions.filter { it.type == TransactionType.REFUND }
                    .sumOf { -it.amount }}",
                icon = R.drawable.receipt_long_24px
            )
            SummaryItem(
                title = "Pending",
                value = transactions.count { it.status == TransactionStatus.PENDING }.toString(),
                icon = R.drawable.brand_sports_24px
            )
        }
    }
}

@Composable
private fun SummaryItem(
    title: String,
    value: String,
    icon: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color(0xFF006D40),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006D40)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF006D40)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionCard(
    transaction: PaymentTransaction,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.date.format(
                        DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm")
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
                Text(
                    text = transaction.paymentMethod,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (transaction.type == TransactionType.REFUND)
                        "-₹${String.format("%.2f", -transaction.amount)}"
                    else
                        "₹${String.format("%.2f", transaction.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (transaction.type == TransactionType.REFUND)
                        Color(0xFF006D40)
                    else
                        Color(0xFF332D25)
                )
                StatusChip(transaction.status)
            }
        }
    }
}

@Composable
private fun StatusChip(status: TransactionStatus) {
    val (backgroundColor, textColor) = when (status) {
        TransactionStatus.COMPLETED -> Color(0xFFE7F5EC) to Color(0xFF006D40)
        TransactionStatus.PENDING -> Color(0xFFFFF7E6) to Color(0xFFB25E02)
        TransactionStatus.PROCESSING -> Color(0xFFE5F6FF) to Color(0xFF0073CC)
        TransactionStatus.FAILED -> Color(0xFFFFEBEB) to Color(0xFFCC0000)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name.lowercase().capitalize(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

@Composable
private fun TransactionDetailsDialog(
    transaction: PaymentTransaction,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Transaction Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailItem("Transaction ID", transaction.id)
                DetailItem("Amount",
                    if (transaction.type == TransactionType.REFUND)
                        "-₹${String.format("%.2f", -transaction.amount)}"
                    else
                        "₹${String.format("%.2f", transaction.amount)}"
                )
                DetailItem("Type", transaction.type.name)
                DetailItem("Status", transaction.status.name)
                DetailItem("Date", transaction.date.format(
                    DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm")
                ))
                DetailItem("Payment Method", transaction.paymentMethod)
                DetailItem("Description", transaction.description)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF637478)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    currentFilter: TransactionFilter,
    onFilterChange: (TransactionFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTypes by remember { mutableStateOf(currentFilter.types) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Filter Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Transaction Type",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            TransactionType.values().forEach { type ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = type in selectedTypes,
                        onCheckedChange = {
                            selectedTypes = if (it) {
                                selectedTypes + type
                            } else {
                                selectedTypes - type
                            }
                        }
                    )
                    Text(type.name.lowercase().capitalize())
                }
            }

            Button(
                onClick = {
                    onFilterChange(currentFilter.copy(types = selectedTypes))
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                Text("Apply Filters")
            }

            TextButton(
                onClick = {
                    selectedTypes = emptySet()
                    onFilterChange(TransactionFilter())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Filters")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}