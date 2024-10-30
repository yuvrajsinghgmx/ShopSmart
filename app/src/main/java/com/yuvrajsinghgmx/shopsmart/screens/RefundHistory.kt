package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class RefundTransaction(
    val id: String,
    val orderNumber: String,
    val amount: Double,
    val date: LocalDate,
    val status: RefundStatus,
    val method: String
)

enum class RefundStatus {
    PENDING, PROCESSING, COMPLETED, FAILED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundHistoryScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf<RefundStatus?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Sample data
    val refunds = remember {
        listOf(
            RefundTransaction(
                "1", "ORD-123456", 299.99,
                LocalDate.now().minusDays(1),
                RefundStatus.COMPLETED, "Original Payment Method"
            ),
            RefundTransaction(
                "2", "ORD-123457", 149.99,
                LocalDate.now().minusDays(2),
                RefundStatus.PENDING, "Store Credit"
            ),
            RefundTransaction(
                "3", "ORD-123458", 499.99,
                LocalDate.now().minusDays(3),
                RefundStatus.PROCESSING, "Bank Account"
            ),
            RefundTransaction(
                "4", "ORD-123459", 79.99,
                LocalDate.now().minusDays(5),
                RefundStatus.FAILED, "Original Payment Method"
            )
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Refund History",
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
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_list_24px),
                            contentDescription = "Filter"
                        )
                    }
                    IconButton(onClick = { /* Download functionality */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.download_24px),
                            contentDescription = "Download"
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
        if (refunds.isEmpty()) {
            EmptyRefundHistory(modifier = Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(refunds.size) { index ->
                    RefundHistoryItem(refund = refunds[index])
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            currentFilter = selectedFilter,
            onFilterSelected = { filter ->
                selectedFilter = filter
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
private fun RefundHistoryItem(refund: RefundTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Text(
                    text = refund.orderNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                StatusChip(status = refund.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "₹${String.format("%.2f", refund.amount)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = refund.method,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = refund.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: RefundStatus) {
    val (backgroundColor, contentColor) = when (status) {
        RefundStatus.COMPLETED -> Color(0xFFE7F5EC) to Color(0xFF006D40)
        RefundStatus.PENDING -> Color(0xFFFFF7E6) to Color(0xFFB25E02)
        RefundStatus.PROCESSING -> Color(0xFFE5F6FF) to Color(0xFF0073CC)
        RefundStatus.FAILED -> Color(0xFFFFEBEB) to Color(0xFFCC0000)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
private fun EmptyRefundHistory(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
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
            text = "No Refund History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your refund history will appear here",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun FilterDialog(
    currentFilter: RefundStatus?,
    onFilterSelected: (RefundStatus?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Refunds") },
        text = {
            Column {
                RadioButton(
                    selected = currentFilter == null,
                    onClick = { onFilterSelected(null) }
                )
                Text("All Refunds")

                RefundStatus.entries.forEach { status ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentFilter == status,
                            onClick = { onFilterSelected(status) }
                        )
                        Text(status.name.lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}