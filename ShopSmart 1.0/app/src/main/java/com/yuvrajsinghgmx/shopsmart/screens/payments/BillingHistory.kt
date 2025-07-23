package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.text.SimpleDateFormat
import java.util.*

data class BillingRecord(
    val id: String,
    val orderId: String,
    val amount: Double,
    val date: Long,
    val paymentMethod: String,
    val status: PaymentStatus,
    val items: List<PurchasedItem>,
    val billingAddress: String,
    val invoiceUrl: String
)

data class PurchasedItem(
    val name: String,
    val quantity: Int,
    val price: Double
)

enum class PaymentStatus {
    PAID, PENDING, FAILED, REFUNDED
}

enum class TimeFilter {
    ALL, LAST_MONTH, LAST_3_MONTHS, LAST_6_MONTHS, LAST_YEAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingHistoryScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)
    var selectedTimeFilter by remember { mutableStateOf(TimeFilter.ALL) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedRecord by remember { mutableStateOf<BillingRecord?>(null) }

    // Sample billing records
    var billingRecords by remember {
        mutableStateOf(
            listOf(
                BillingRecord(
                    "1",
                    "ORD123456",
                    2499.99,
                    System.currentTimeMillis() - 86400000,
                    "VISA ending in 1234",
                    PaymentStatus.PAID,
                    listOf(
                        PurchasedItem("Smartphone", 1, 1999.99),
                        PurchasedItem("Phone Case", 1, 500.00)
                    ),
                    "123 Main Street, City",
                    "invoice_123.pdf"
                ),
                BillingRecord(
                    "2",
                    "ORD123457",
                    799.99,
                    System.currentTimeMillis() - 172800000,
                    "Wallet Balance",
                    PaymentStatus.REFUNDED,
                    listOf(
                        PurchasedItem("Headphones", 1, 799.99)
                    ),
                    "123 Main Street, City",
                    "invoice_124.pdf"
                ),
                BillingRecord(
                    "3",
                    "ORD123458",
                    1299.99,
                    System.currentTimeMillis() - 259200000,
                    "PayPal",
                    PaymentStatus.PAID,
                    listOf(
                        PurchasedItem("Smart Watch", 1, 1299.99)
                    ),
                    "123 Main Street, City",
                    "invoice_125.pdf"
                )
            )
        )
    }

    // Filter records based on selected time filter
    val filteredRecords = when (selectedTimeFilter) {
        TimeFilter.LAST_MONTH -> billingRecords.filter {
            it.date > System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
        }
        TimeFilter.LAST_3_MONTHS -> billingRecords.filter {
            it.date > System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000
        }
        TimeFilter.LAST_6_MONTHS -> billingRecords.filter {
            it.date > System.currentTimeMillis() - 180L * 24 * 60 * 60 * 1000
        }
        TimeFilter.LAST_YEAR -> billingRecords.filter {
            it.date > System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000
        }
        TimeFilter.ALL -> billingRecords
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = lightBackgroundColor,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF332D25)
                        )
                    }

                    Text(
                        text = "Billing History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25),
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )

                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_list_24px),
                            contentDescription = "Filter",
                            tint = Color(0xFF332D25)
                        )
                    }
                }
            }

            // Main Content
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (filteredRecords.isEmpty()) {
                    EmptyBillingHistory()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredRecords) { record ->
                            BillingRecordCard(
                                record = record,
                                onClick = { selectedRecord = record }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter by Time") },
            text = {
                Column {
                    TimeFilter.values().forEach { filter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTimeFilter == filter,
                                onClick = { selectedTimeFilter = filter }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (filter) {
                                    TimeFilter.ALL -> "All Time"
                                    TimeFilter.LAST_MONTH -> "Last Month"
                                    TimeFilter.LAST_3_MONTHS -> "Last 3 Months"
                                    TimeFilter.LAST_6_MONTHS -> "Last 6 Months"
                                    TimeFilter.LAST_YEAR -> "Last Year"
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Done")
                }
            }
        )
    }

    // Record Detail Dialog
    if (selectedRecord != null) {
        BillingDetailDialog(
            record = selectedRecord!!,
            onDismiss = { selectedRecord = null },
            onDownloadInvoice = { /* Handle invoice download */ }
        )
    }
}

@Composable
private fun BillingRecordCard(
    record: BillingRecord,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${record.orderId}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(Date(record.date)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = "₹${String.format("%.2f", record.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.paymentMethod,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                PaymentStatusChip(status = record.status)
            }
        }
    }
}

@Composable
private fun PaymentStatusChip(status: PaymentStatus) {
    AssistChip(
        onClick = { },
        label = { Text(status.name) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = when (status) {
                PaymentStatus.PAID -> Color(0xFF0E8545).copy(alpha = 0.1f)
                PaymentStatus.PENDING -> MaterialTheme.colorScheme.primaryContainer
                PaymentStatus.FAILED -> MaterialTheme.colorScheme.errorContainer
                PaymentStatus.REFUNDED -> MaterialTheme.colorScheme.tertiaryContainer
            },
            labelColor = when (status) {
                PaymentStatus.PAID -> Color(0xFF0E8545)
                PaymentStatus.PENDING -> MaterialTheme.colorScheme.primary
                PaymentStatus.FAILED -> MaterialTheme.colorScheme.error
                PaymentStatus.REFUNDED -> MaterialTheme.colorScheme.tertiary
            }
        )
    )
}

@Composable
private fun BillingDetailDialog(
    record: BillingRecord,
    onDismiss: () -> Unit,
    onDownloadInvoice: (BillingRecord) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Order Details")
                IconButton(onClick = { onDownloadInvoice(record) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.download_24px),
                        contentDescription = "Download Invoice"
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailSection(
                    title = "Order Information",
                    content = listOf(
                        "Order ID" to "#${record.orderId}",
                        "Date" to SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(Date(record.date)),
                        "Status" to record.status.name
                    )
                )

                DetailSection(
                    title = "Items",
                    content = record.items.map {
                        "${it.name} (${it.quantity}x)" to "₹${String.format("%.2f", it.price)}"
                    }
                )

                DetailSection(
                    title = "Payment",
                    content = listOf(
                        "Method" to record.paymentMethod,
                        "Total Amount" to "₹${String.format("%.2f", record.amount)}"
                    )
                )

                DetailSection(
                    title = "Billing Address",
                    content = listOf("Address" to record.billingAddress)
                )
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
private fun DetailSection(
    title: String,
    content: List<Pair<String, String>>
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        content.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun EmptyBillingHistory() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.receipt_long_24px),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Billing History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your billing history will appear here",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}