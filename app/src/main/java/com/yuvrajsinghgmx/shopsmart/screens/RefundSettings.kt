package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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

data class RefundMethod(
    val id: String,
    val title: String,
    val subtitle: String,
    val isDefault: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundSettingsScreen(navController: NavController) {
    var showAddMethodDialog by remember { mutableStateOf(false) }
    var selectedMethod by remember { mutableStateOf<RefundMethod?>(null) }

    // Sample refund methods
    var refundMethods by remember {
        mutableStateOf(
            listOf(
                RefundMethod("1", "Original Payment Method", "Refund to the original payment method", true),
                RefundMethod("2", "Store Credit", "Receive store credit for faster refunds", false),
                RefundMethod("3", "Bank Account", "Direct deposit to linked bank account", false)
            )
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Refund Settings",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBackgroundColor
                )
            )
        },
        containerColor = lightBackgroundColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Default Refund Method",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(refundMethods.size) { index ->
                RefundMethodItem(
                    method = refundMethods[index],
                    onSetDefault = { method ->
                        refundMethods = refundMethods.map { it.copy(isDefault = it.id == method.id) }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Refund Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                RefundPreferencesSection()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Additional Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                AdditionalSettingsSection()
            }
        }
    }
}

@Composable
private fun RefundMethodItem(
    method: RefundMethod,
    onSetDefault: (RefundMethod) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = method.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = method.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            if (method.isDefault) {
                Surface(
                    modifier = Modifier.height(32.dp),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.check_circle_24px),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Default",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                TextButton(
                    onClick = { onSetDefault(method) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF006D40) // Green color from your design
                    )
                ) {
                    Text(
                        "Set as Default",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun RefundPreferencesSection() {
    var automaticRefunds by remember { mutableStateOf(true) }
    var refundNotifications by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Automatic Refunds",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Process eligible refunds automatically",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked = automaticRefunds,
                    onCheckedChange = { automaticRefunds = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Refund Notifications",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Receive updates about refund status",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked = refundNotifications,
                    onCheckedChange = { refundNotifications = it }
                )
            }
        }
    }
}

@Composable
private fun AdditionalSettingsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsItem(
                title = "Refund History",
                subtitle = "View past refund transactions",
                onClick = { }
            )

            HorizontalDivider()

            SettingsItem(
                title = "Refund Policy",
                subtitle = "Read our refund terms and conditions",
                onClick = { }
            )

            HorizontalDivider()

            SettingsItem(
                title = "Contact Support",
                subtitle = "Get help with refunds",
                onClick = { }
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.arrow_forward_24px),
            contentDescription = "Navigate",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}