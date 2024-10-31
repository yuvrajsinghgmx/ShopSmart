package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.AnimatedVisibility
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingRefundScreen(navController: NavController) {
    var orderId by remember { mutableStateOf("") }
    var showDetails by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var additionalNotes by remember { mutableStateOf("") }
    var showSubmitDialog by remember { mutableStateOf(false) }

    val refundIssueOptions = listOf(
        "Refund not received after return accepted",
        "Partial refund received",
        "Refund shown as processed but not credited",
        "Wrong account credited",
        "Refund delayed beyond timeline",
        "Other issues"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Missing Refund",
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
            // Status Check Section
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
                            "Check Refund Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        OutlinedTextField(
                            value = orderId,
                            onValueChange = {
                                orderId = it
                                showDetails = false
                            },
                            label = { Text("Order ID") },
                            placeholder = { Text("Enter Order ID (e.g., ORD-12345678)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF006D40),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )

                        Button(
                            onClick = { showDetails = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF006D40)
                            )
                        ) {
                            Text("Check Status")
                        }
                    }
                }
            }

            // Quick Actions Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFFE7F5EC)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.support_agent_24px),
                            contentDescription = null,
                            tint = Color(0xFF006D40)
                        )
                        Column {
                            Text(
                                "Quick Support Actions",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF006D40)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            QuickActionButton("Live Chat Support", R.drawable.chat_24px) {
                                navController.navigate("live_chat")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            QuickActionButton("Call Support", R.drawable.contact_phone_24px) {
                                navController.navigate("phone_support")
                            }
                        }
                    }
                }
            }

            // Issue Selection
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Select Issue Type",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        refundIssueOptions.forEach { option ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedOption == option,
                                    onClick = { selectedOption = option }
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Additional Notes
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Additional Notes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        OutlinedTextField(
                            value = additionalNotes,
                            onValueChange = { additionalNotes = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            placeholder = { Text("Provide any additional details about your missing refund") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF006D40),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = { showSubmitDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedOption != null && orderId.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.send_24px),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Report")
                }
            }

            // Help Card
            item {
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
                                "Check our refund policy or contact support for immediate assistance",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB25E02)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { navController.navigate("refund_policy") },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFFB25E02)
                                )
                            ) {
                                Text("View Refund Policy")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.check_circle_24px),
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
            },
            title = {
                Text("Report Submitted")
            },
            text = {
                Text("We've received your missing refund report. Our team will investigate and contact you within 24-48 hours.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF006D40)
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
