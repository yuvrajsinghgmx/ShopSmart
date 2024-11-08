package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.yuvrajsinghgmx.shopsmart.profilefeatures.BankAccountManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAccountDetailsScreen(navController: NavController, accountNumber: String) {
    val account = BankAccountManager.getAccount(accountNumber)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDefaultDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5F3))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF6F5F3),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
                            contentDescription = "Back",
                            tint = Color(0xFF332D25)
                        )
                    }

                    Text(
                        text = "Account Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Main Content
            if (account != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Account Status Card
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = if (account.isDefault) Color(0xFFE7F5EC) else Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (account.isDefault)
                                            R.drawable.ic_check_circle
                                        else
                                            R.drawable.account_balance_24px
                                    ),
                                    contentDescription = null,
                                    tint = if (account.isDefault) Color(0xFF006D40) else Color(0xFF637478)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "•••• ${accountNumber.takeLast(4)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (account.isDefault)
                                            "Default Account"
                                        else
                                            "Saved Account",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF637478)
                                    )
                                }
                            }
                        }
                    }

                    // Account Details Card
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
                                    "Account Information",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                DetailItem(
                                    icon = R.drawable.profile,
                                    label = "Account Holder",
                                    value = account.holderName
                                )

                                DetailItem(
                                    icon = R.drawable.account_balance_24px,
                                    label = "Bank Name",
                                    value = account.bankName
                                )

                                DetailItem(
                                    icon = R.drawable.account_balance_wallet_24px,
                                    label = "Account Type",
                                    value = account.accountType
                                )

                                DetailItem(
                                    icon = R.drawable.payments_24px,
                                    label = "IFSC Code",
                                    value = account.ifscCode
                                )

                                DetailItem(
                                    icon = R.drawable.pin_drop_24px,
                                    label = "Branch",
                                    value = account.branchName
                                )
                            }
                        }
                    }

                    // Actions Card
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = Color.White
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (!account.isDefault) {
                                    Button(
                                        onClick = { showDefaultDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF006D40)
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_check_circle),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Set as Default")
                                    }
                                }

                                OutlinedButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Red
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.delete_24px),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Remove Account")
                                }
                            }
                        }
                    }

                    // Security Note
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
                                    painter = painterResource(id = R.drawable.ic_lock),
                                    contentDescription = null,
                                    tint = Color(0xFFB25E02)
                                )
                                Text(
                                    "Your bank account information is securely stored and encrypted",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFB25E02)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remove Bank Account") },
            text = { Text("Are you sure you want to remove this bank account?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        BankAccountManager.removeAccount(accountNumber)
                        showDeleteDialog = false
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Set Default Confirmation Dialog
    if (showDefaultDialog) {
        AlertDialog(
            onDismissRequest = { showDefaultDialog = false },
            title = { Text("Set as Default") },
            text = { Text("Set this as your default bank account?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        BankAccountManager.setDefaultAccount(accountNumber)
                        showDefaultDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("Set as Default")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDefaultDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DetailItem(icon: Int, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color(0xFF637478),
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF637478)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}