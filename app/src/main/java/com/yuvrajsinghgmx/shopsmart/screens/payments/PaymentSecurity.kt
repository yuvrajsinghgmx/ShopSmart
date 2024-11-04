package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSecurityScreen(navController: NavController) {
    var biometricEnabled by remember { mutableStateOf(false) }
    var pinEnabled by remember { mutableStateOf(true) }
    var twoFactorEnabled by remember { mutableStateOf(false) }
    var showChangePin by remember { mutableStateOf(false) }

    val securityAlerts = listOf(
        SecurityAlert("Unusual activity detected", "Login attempt from new device in London", "2 hours ago", AlertType.WARNING),
        SecurityAlert("Security tip", "Enable two-factor authentication for extra security", "1 day ago", AlertType.INFO),
        SecurityAlert("Successful login", "New login from your iPhone", "2 days ago", AlertType.SUCCESS)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Payment Security",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Quick Actions Section
            item {
                SecurityQuickActions(navController)
            }

            // Authentication Methods Section
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
                            "Authentication Methods",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Biometric Authentication
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
                                    painter = painterResource(id = R.drawable.policy_24px),
                                    contentDescription = null,
                                    tint = Color(0xFF637478)
                                )
                                Column {
                                    Text(
                                        "Biometric Authentication",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Use fingerprint or face ID",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF637478)
                                    )
                                }
                            }
                            Switch(
                                checked = biometricEnabled,
                                onCheckedChange = { biometricEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF006D40),
                                    checkedTrackColor = Color(0xFFE7F5EC)
                                )
                            )
                        }

                        HorizontalDivider()

                        // PIN Authentication
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
                                    painter = painterResource(id = R.drawable.pin_drop_24px),
                                    contentDescription = null,
                                    tint = Color(0xFF637478)
                                )
                                Column {
                                    Text(
                                        "PIN Authentication",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Use 6-digit PIN",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF637478)
                                    )
                                }
                            }
                            Switch(
                                checked = pinEnabled,
                                onCheckedChange = { pinEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF006D40),
                                    checkedTrackColor = Color(0xFFE7F5EC)
                                )
                            )
                        }

                        if (pinEnabled) {
                            TextButton(
                                onClick = { showChangePin = true }
                            ) {
                                Text("Change PIN", color = Color(0xFF006D40))
                            }
                        }

                        HorizontalDivider()

                        // Two-Factor Authentication
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
                                    painter = painterResource(id = R.drawable.verified_user_24px),
                                    contentDescription = null,
                                    tint = Color(0xFF637478)
                                )
                                Column {
                                    Text(
                                        "Two-Factor Authentication",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Additional security layer",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF637478)
                                    )
                                }
                            }
                            Switch(
                                checked = twoFactorEnabled,
                                onCheckedChange = {
                                    // Navigate to 2FA setup screen
                                    navController.navigate("coming_soon")
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF006D40),
                                    checkedTrackColor = Color(0xFFE7F5EC)
                                )
                            )
                        }
                    }
                }
            }

            // Recent Security Alerts
            item {
                Text(
                    "Recent Security Alerts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(securityAlerts) { alert ->
                SecurityAlertItem(alert)
            }

            // Additional Security Options
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
                            "Additional Security",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        SecurityOptionItem(
                            icon = R.drawable.account_balance_wallet_24px,
                            title = "Trusted Devices",
                            subtitle = "Manage devices that can access your account",
                            onClick = { navController.navigate("coming_soon") }
                        )

                        SecurityOptionItem(
                            icon = R.drawable.notifications_24px,
                            title = "Security Notifications",
                            subtitle = "Manage alert preferences",
                            onClick = { navController.navigate("coming_soon") }
                        )

                        SecurityOptionItem(
                            icon = R.drawable.history_24px,
                            title = "Activity Log",
                            subtitle = "View recent account activities",
                            onClick = { navController.navigate("coming_soon") }
                        )
                    }
                }
            }
        }
    }

    if (showChangePin) {
        ChangePinDialog(
            onDismiss = { showChangePin = false },
            onPinChanged = { /* Handle PIN change */ }
        )
    }
}

@Composable
private fun SecurityQuickActions(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton(
                icon = R.drawable.admin_panel_settings_24px,
                text = "Security\nScan",
                onClick = { navController.navigate("security_scan") }
            )
            QuickActionButton(
                icon = R.drawable.password_24px,
                text = "Change\nPassword",
                onClick = { navController.navigate("change_password") }
            )
            QuickActionButton(
                icon = R.drawable.verified_user_24px,
                text = "Privacy\nCheck",
                onClick = { navController.navigate("privacy_check") }
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

enum class AlertType {
    WARNING, INFO, SUCCESS
}

data class SecurityAlert(
    val title: String,
    val description: String,
    val time: String,
    val type: AlertType
)

@Composable
private fun SecurityAlertItem(alert: SecurityAlert) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = when (alert.type) {
            AlertType.WARNING -> Color(0xFFFFF4ED)
            AlertType.INFO -> Color(0xFFF0F7FF)
            AlertType.SUCCESS -> Color(0xFFE7F5EC)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = when (alert.type) {
                        AlertType.WARNING -> R.drawable.info_24px
                        AlertType.INFO -> R.drawable.info_24px
                        AlertType.SUCCESS -> R.drawable.check_circle_24px
                    }
                ),
                contentDescription = null,
                tint = when (alert.type) {
                    AlertType.WARNING -> Color(0xFFB25E02)
                    AlertType.INFO -> Color(0xFF0055D4)
                    AlertType.SUCCESS -> Color(0xFF006D40)
                }
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    alert.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    alert.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    alert.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityOptionItem(
    icon: Int,
    title: String,
    subtitle: String,
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
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF637478)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_24px),
                contentDescription = "Navigate",
                tint = Color(0xFF637478)
            )
        }
    }
}

@Composable
private fun ChangePinDialog(
    onDismiss: () -> Unit,
    onPinChanged: (String) -> Unit
) {
    var currentPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Change PIN",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = currentPin,
                    onValueChange = {
                        if (it.length <= 6) currentPin = it
                        showError = false
                    },
                    label = { Text("Current PIN") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPin,
                    onValueChange = {
                        if (it.length <= 6) newPin = it
                        showError = false
                    },
                    label = { Text("New PIN") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = {
                        if (it.length <= 6) confirmPin = it
                        showError = false
                    },
                    label = { Text("Confirm New PIN") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError
                )

                if (showError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        currentPin.length != 6 -> {
                            showError = true
                            errorMessage = "Current PIN must be 6 digits"
                        }
                        newPin.length != 6 -> {
                            showError = true
                            errorMessage = "New PIN must be 6 digits"
                        }
                        newPin != confirmPin -> {
                            showError = true
                            errorMessage = "PINs don't match"
                        }
                        currentPin == newPin -> {
                            showError = true
                            errorMessage = "New PIN must be different from current PIN"
                        }
                        else -> {
                            onPinChanged(newPin)
                            onDismiss()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                Text("Change PIN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}