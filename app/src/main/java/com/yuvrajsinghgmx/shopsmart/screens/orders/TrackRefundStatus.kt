package com.yuvrajsinghgmx.shopsmart.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class RefundTrackingStatus {
    PENDING, PROCESSING, COMPLETED, REJECTED
}

data class RefundDetails(
    val orderId: String,
    val amount: Double,
    val requestDate: LocalDateTime,
    val currentStatus: RefundTrackingStatus,
    val expectedDate: LocalDateTime?,
    val refundMethod: String,
    val updates: List<StatusUpdate>
)

data class StatusUpdate(
    val title: String,
    val description: String,
    val timestamp: LocalDateTime,
    val isComplete: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackRefundScreen(navController: NavController) {
    var orderIdInput by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var trackingDetails by remember { mutableStateOf<RefundDetails?>(null) }

    // Sample data
    val mockRefund = RefundDetails(
        orderId = "ORD-12345678",
        amount = 299.99,
        requestDate = LocalDateTime.now().minusDays(3),
        currentStatus = RefundTrackingStatus.PROCESSING,
        expectedDate = LocalDateTime.now().plusDays(2),
        refundMethod = "Original Payment Method",
        updates = listOf(
            StatusUpdate(
                "Refund Requested",
                "Your refund request has been received",
                LocalDateTime.now().minusDays(3),
                true
            ),
            StatusUpdate(
                "Request Approved",
                "Your refund request has been approved",
                LocalDateTime.now().minusDays(2),
                true
            ),
            StatusUpdate(
                "Processing",
                "Refund is being processed",
                LocalDateTime.now().minusDays(1),
                false
            )
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Track Refund",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
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
            item {
                // Search Section
                Column {
                    OutlinedTextField(
                        value = orderIdInput,
                        onValueChange = { orderIdInput = it },
                        label = { Text("Order ID") },
                        placeholder = { Text("Enter Order ID (e.g., ORD-12345678)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF637478)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        isError = showError,
                        supportingText = if (showError) {
                            { Text("No refund found for this Order ID") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF006D40),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (orderIdInput == mockRefund.orderId) {
                                trackingDetails = mockRefund
                                showError = false
                            } else {
                                showError = true
                                trackingDetails = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006D40)
                        )
                    ) {
                        Text("Track Refund")
                    }
                }
            }

            trackingDetails?.let { refund ->
                // Status Card
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "Order ID",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF637478)
                                    )
                                    Text(
                                        refund.orderId,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "Amount",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF637478)
                                    )
                                    Text(
                                        "₹${String.format("%.2f", refund.amount)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF006D40)
                                    )
                                }
                            }

                            StatusBadge(refund.currentStatus)

                            if (refund.expectedDate != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Expected Credit Date",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF637478)
                                    )
                                    Text(
                                        refund.expectedDate.format(
                                            DateTimeFormatter.ofPattern("dd MMM, yyyy")
                                        ),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Timeline Card
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "Refund Timeline",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            refund.updates.forEachIndexed { index, update ->
                                TimelineItem(
                                    update = update,
                                    isLast = index == refund.updates.size - 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: RefundTrackingStatus) {
    val (backgroundColor, textColor) = when (status) {
        RefundTrackingStatus.COMPLETED -> Color(0xFFE7F5EC) to Color(0xFF006D40)
        RefundTrackingStatus.PROCESSING -> Color(0xFFE5F6FF) to Color(0xFF0073CC)
        RefundTrackingStatus.PENDING -> Color(0xFFFFF7E6) to Color(0xFFB25E02)
        RefundTrackingStatus.REJECTED -> Color(0xFFFFEBEB) to Color(0xFFCC0000)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = when (status) {
                RefundTrackingStatus.COMPLETED -> "Refund Completed"
                RefundTrackingStatus.PROCESSING -> "Processing Refund"
                RefundTrackingStatus.PENDING -> "Refund Pending"
                RefundTrackingStatus.REJECTED -> "Refund Rejected"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}

@Composable
private fun TimelineItem(
    update: StatusUpdate,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(20.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (update.isComplete) Color(0xFF006D40) else Color(0xFFE5E7EB),
                modifier = Modifier.size(20.dp)
            ) {
                if (update.isComplete) {
                    Icon(
                        painter = painterResource(id = R.drawable.check_circle_24px),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(
                            if (update.isComplete) Color(0xFF006D40) else Color(0xFFE5E7EB)
                        )
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                update.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (update.isComplete) Color(0xFF006D40) else Color(0xFF637478)
            )
            Text(
                update.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
            Text(
                update.timestamp.format(
                    DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm")
                ),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF637478)
            )
        }
    }
}
