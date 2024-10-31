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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.profilefeatures.Transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(navController: NavController) {
    // Sample transactions
    val transactions = remember {
        listOf(
            Transaction(
                "TRX-001",
                299.99,
                LocalDateTime.now().minusDays(1),
                "Electronics Purchase",
                "PURCHASE",
                "COMPLETED",
                "Credit Card ****1234"
            ),
            Transaction(
                "TRX-002",
                49.99,
                LocalDateTime.now().minusDays(2),
                "Product Return Refund",
                "REFUND",
                "COMPLETED",
                "Original Payment Method"
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Transaction History",
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

            // Transactions List
            items(transactions) { transaction ->
                TransactionCard(transaction)
            }
        }
    }
}

@Composable
private fun TransactionSummaryCard(transactions: List<Transaction>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Total Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF006D40)
                )
                Text(
                    "${transactions.size}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006D40)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "Total Amount",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF006D40)
                )
                Text(
                    "₹${transactions.sumOf { it.amount }}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006D40)
                )
            }
        }
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (transaction.type == "REFUND") "-₹${transaction.amount}"
                    else "₹${transaction.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (transaction.type == "REFUND") Color(0xFF006D40)
                    else Color(0xFF332D25)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transaction.paymentMethod,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )
                Text(
                    text = transaction.date.format(
                        DateTimeFormatter.ofPattern("dd MMM, yyyy")
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            StatusChip(transaction.status)
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "COMPLETED" -> Color(0xFFE7F5EC) to Color(0xFF006D40)
        "PENDING" -> Color(0xFFFFF7E6) to Color(0xFFB25E02)
        else -> Color(0xFFFFEBEB) to Color(0xFFCC0000)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.lowercase().capitalize(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}