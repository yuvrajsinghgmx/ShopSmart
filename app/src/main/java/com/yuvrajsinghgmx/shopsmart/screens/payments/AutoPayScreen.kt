package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

data class AutoPayment(
    val id: String,
    val name: String,
    val amount: Double,
    val frequency: String,
    val nextPaymentDate: String,
    val paymentMethod: String,
    var isEnabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoPayScreen(navController: NavController) {
    var showAddPaymentDialog by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<AutoPayment?>(null) }
    var autoPayments by remember {
        mutableStateOf(
            listOf(
                AutoPayment(
                    "1",
                    "Netflix Subscription",
                    14.99,
                    "Monthly",
                    "Feb 15, 2024",
                    "•••• 5678"
                ),
                AutoPayment(
                    "2",
                    "Gym Membership",
                    49.99,
                    "Monthly",
                    "Feb 20, 2024",
                    "•••• 1234"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Auto-Pay",
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
        containerColor = Color(0xFFF6F5F3),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPaymentDialog = true },
                containerColor = Color(0xFF006D40)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Auto Payment",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        if (autoPayments.isEmpty()) {
            EmptyAutoPayState(
                modifier = Modifier.padding(innerPadding),
                onAddPayment = { showAddPaymentDialog = true }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overview Card
                item {
                    AutoPayOverviewCard(autoPayments)
                }

                // Settings Card
                item {
                    AutoPaySettingsCard(navController)
                }

                // Active Auto-Payments
                item {
                    Text(
                        "Active Auto-Payments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(autoPayments) { payment ->
                    AutoPaymentCard(
                        payment = payment,
                        onEditClick = { selectedPayment = payment },
                        onToggle = { isEnabled ->
                            autoPayments = autoPayments.map {
                                if (it.id == payment.id) it.copy(isEnabled = isEnabled)
                                else it
                            }
                        }
                    )
                }

                // Info Card
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
                                    "Auto-Pay Information",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0055D4)
                                )
                                Text(
                                    "Automatic payments are processed on the scheduled date. Make sure to maintain sufficient balance.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF0055D4)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dialogs
        if (showAddPaymentDialog) {
            AddAutoPaymentDialog(
                onDismiss = { showAddPaymentDialog = false },
                onSave = { payment ->
                    autoPayments = autoPayments + payment
                    showAddPaymentDialog = false
                }
            )
        }

        selectedPayment?.let { payment ->
            EditAutoPaymentDialog(
                payment = payment,
                onDismiss = { selectedPayment = null },
                onSave = { updatedPayment ->
                    autoPayments = autoPayments.map {
                        if (it.id == updatedPayment.id) updatedPayment
                        else it
                    }
                    selectedPayment = null
                },
                onDelete = { paymentToDelete ->
                    autoPayments = autoPayments.filter { it.id != paymentToDelete.id }
                    selectedPayment = null
                }
            )
        }
    }
}

@Composable
private fun EmptyAutoPayState(
    modifier: Modifier = Modifier,
    onAddPayment: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.payments_24px),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF637478)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No Auto-Payments Set Up",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Set up automatic payments for your recurring bills",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF637478)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddPayment,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006D40)
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Auto Payment")
        }
    }
}

@Composable
private fun AutoPayOverviewCard(autoPayments: List<AutoPayment>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Active Auto-Payments",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        autoPayments.count { it.isEnabled }.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Total Monthly",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        NumberFormat.getCurrencyInstance(Locale.US)
                            .format(autoPayments.sumOf { it.amount }),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AutoPaySettingsCard(navController: NavController) {
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
                "Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.notifications_24px),
                        contentDescription = null,
                        tint = Color(0xFF637478)
                    )
                    Column {
                        Text(
                            "Payment Reminders",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Get notified before auto-payments",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF637478)
                        )
                    }
                }
                IconButton(onClick = { navController.navigate("coming_soon") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_forward_24px),
                        contentDescription = "Navigate",
                        tint = Color(0xFF637478)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_month_24px),
                        contentDescription = null,
                        tint = Color(0xFF637478)
                    )
                    Column {
                        Text(
                            "Payment Schedule",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "View upcoming payments",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF637478)
                        )
                    }
                }
                IconButton(onClick = { navController.navigate("coming_soon") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_forward_24px),
                        contentDescription = "Navigate",
                        tint = Color(0xFF637478)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoPaymentCard(
    payment: AutoPayment,
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.payments_24px),
                        contentDescription = null,
                        tint = Color(0xFF637478),
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            payment.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${payment.frequency} - ${
                                NumberFormat.getCurrencyInstance(Locale.US)
                                    .format(payment.amount)
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF637478)
                        )
                    }
                }
                Switch(
                    checked = payment.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF006D40),
                        checkedTrackColor = Color(0xFFE7F5EC)
                    )
                )
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_month_24px),
                        contentDescription = null,
                        tint = Color(0xFF637478),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "Next payment: ${payment.nextPaymentDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.credit_card_24px),
                        contentDescription = null,
                        tint = Color(0xFF637478),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        payment.paymentMethod,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAutoPaymentDialog(
    onDismiss: () -> Unit,
    onSave: (AutoPayment) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Monthly") }
    var paymentDate by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add Auto Payment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Payment Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Frequency") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = paymentDate,
                    onValueChange = { paymentDate = it },
                    label = { Text("Payment Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text("Payment Method") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && amount.isNotBlank()) {
                        onSave(
                            AutoPayment(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                frequency = frequency,
                                nextPaymentDate = paymentDate,
                                paymentMethod = paymentMethod
                            )
                        )
                    }
                },
                enabled = name.isNotBlank() && amount.isNotBlank(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAutoPaymentDialog(
    payment: AutoPayment,
    onDismiss: () -> Unit,
    onSave: (AutoPayment) -> Unit,
    onDelete: (AutoPayment) -> Unit
) {
    var name by remember { mutableStateOf(payment.name) }
    var amount by remember { mutableStateOf(payment.amount.toString()) }
    var frequency by remember { mutableStateOf(payment.frequency) }
    var paymentDate by remember { mutableStateOf(payment.nextPaymentDate) }
    var paymentMethod by remember { mutableStateOf(payment.paymentMethod) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Edit Auto Payment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Payment Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Frequency") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = paymentDate,
                    onValueChange = { paymentDate = it },
                    label = { Text("Payment Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text("Payment Method") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = { onDelete(payment) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFB3261E)
                    )
                ) {
                    Text("Delete")
                }
                Button(
                    onClick = {
                        onSave(
                            payment.copy(
                                name = name,
                                amount = amount.toDoubleOrNull() ?: payment.amount,
                                frequency = frequency,
                                nextPaymentDate = paymentDate,
                                paymentMethod = paymentMethod
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}