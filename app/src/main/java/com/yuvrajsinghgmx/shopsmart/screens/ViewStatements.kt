package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Statement(
    val id: String,
    val month: YearMonth,
    val totalTransactions: Int,
    val totalAmount: Double,
    val downloadUrl: String,
    val isDownloaded: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewStatementsScreen(navController: NavController) {
    var selectedYear by remember { mutableStateOf(YearMonth.now().year) }
    var showYearPicker by remember { mutableStateOf(false) }

    // Sample statements (same as before)
    val statements = remember {
        val currentMonth = YearMonth.now()
        List(12) { index ->
            val month = currentMonth.minusMonths(index.toLong())
            Statement(
                id = "STMT-${month.format(DateTimeFormatter.ofPattern("yyyyMM"))}",
                month = month,
                totalTransactions = (10..30).random(),
                totalAmount = (1000..5000).random().toDouble(),
                downloadUrl = "https://example.com/statements/${month}",
                isDownloaded = index > 6
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Statements",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
                            contentDescription = "Back",
                            tint = Color(0xFF332D25)
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
            // Year Selection Card
            item {
                YearSelectionCard(
                    selectedYear = selectedYear,
                    onYearClick = { showYearPicker = true }
                )
            }

            // Summary Card
            item {
                StatementSummaryCard(statements)
            }

            // Statements List
            items(statements.filter { it.month.year == selectedYear }) { statement ->
                StatementCard(statement = statement)
            }
        }
    }

    // Year Picker Dialog
    if (showYearPicker) {
        YearPickerDialog(
            currentYear = selectedYear,
            onYearSelected = {
                selectedYear = it
                showYearPicker = false
            },
            onDismiss = { showYearPicker = false }
        )
    }
}

@Composable
private fun YearSelectionCard(
    selectedYear: Int,
    onYearClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_month_24px),
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
                Text(
                    "Year",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF332D25)
                )
            }
            TextButton(onClick = onYearClick) {
                Text(
                    selectedYear.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF006D40)
                )
            }
        }
    }
}

@Composable
private fun StatementSummaryCard(statements: List<Statement>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Available Statements",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF006D40)
                )
                Text(
                    statements.size.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006D40)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Downloaded",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF006D40)
                )
                Text(
                    statements.count { it.isDownloaded }.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006D40)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatementCard(statement: Statement) {
    Surface(
        onClick = { /* Handle statement view */ },
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
            Column {
                Text(
                    statement.month.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "${statement.totalTransactions} transactions",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                    Text(
                        "₹${String.format("%.2f", statement.totalAmount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                }
            }
            IconButton(
                onClick = { /* Handle download */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download_24px),
                    contentDescription = "Download",
                    tint = if (statement.isDownloaded) Color(0xFF637478) else Color(0xFF006D40)
                )
            }
        }
    }
}

@Composable
private fun YearPickerDialog(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Select Year",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (currentYear downTo currentYear - 4).forEach { year ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onYearSelected(year) },
                        color = if (year == currentYear)
                            Color(0xFFE7F5EC)
                        else
                            Color.Transparent
                    ) {
                        Text(
                            year.toString(),
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (year == currentYear)
                                Color(0xFF006D40)
                            else
                                Color(0xFF332D25)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}