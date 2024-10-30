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
    var refundMethods by remember {
        mutableStateOf(
            listOf(
                RefundMethod("1", "Original Payment Method", "Refund to the original payment method", true),
                RefundMethod("2", "Store Credit", "Receive store credit for faster refunds", false),
                RefundMethod("3", "Bank Account", "Direct deposit to linked bank account", false)
            )
        )
    }

    val backgroundColor = Color(0xFFF6F5F3)

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
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "Default Refund Method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332D25)
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
                Text(
                    "Refund Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332D25)
                )
            }

            item {
                RefundPreferencesSection()
            }

            item {
                Text(
                    "Additional Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332D25)
                )
            }

            item {
                AdditionalSettingsSection(navController)
            }

            // Add some bottom padding
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun RefundMethodItem(
    method: RefundMethod,
    onSetDefault: (RefundMethod) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium
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
                    text = method.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF332D25)
                )
                Text(
                    text = method.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )
            }

            if (method.isDefault) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.check_circle_24px),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF006D40)
                        )
                        Text(
                            "Default",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF332D25)
                        )
                    }
                }
            } else {
                TextButton(
                    onClick = { onSetDefault(method) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF006D40)
                    )
                ) {
                    Text("Set as Default")
                }
            }
        }
    }
}

@Composable
private fun RefundPreferencesSection() {
    var automaticRefunds by remember { mutableStateOf(true) }
    var refundNotifications by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PreferenceItem(
                title = "Automatic Refunds",
                subtitle = "Process eligible refunds automatically",
                checked = automaticRefunds,
                onCheckedChange = { automaticRefunds = it }
            )

            PreferenceItem(
                title = "Refund Notifications",
                subtitle = "Receive updates about refund status",
                checked = refundNotifications,
                onCheckedChange = { refundNotifications = it }
            )
        }
    }
}

@Composable
private fun PreferenceItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF332D25)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF006D40),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}

@Composable
private fun AdditionalSettingsSection(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingsItem(
                title = "Refund History",
                subtitle = "View past refund transactions",
                onClick = { navController.navigate("refund_history") }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE5E7EB)
            )

            SettingsItem(
                title = "Refund Policy",
                subtitle = "Read our refund terms and conditions",
                onClick = { navController.navigate("refund_policy") }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE5E7EB)
            )

            SettingsItem(
                title = "Contact Support",
                subtitle = "Get help with refunds",
                onClick = { navController.navigate("contact_support") }
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF332D25)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
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