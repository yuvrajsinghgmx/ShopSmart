package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import com.yuvrajsinghgmx.shopsmart.profilefeatures.BankAccountInfo
import com.yuvrajsinghgmx.shopsmart.profilefeatures.BankAccountManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBankAccountScreen(navController: NavController) {
    var accountNumber by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("Savings") }
    var bankName by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var branchName by remember { mutableStateOf("") }
    var makeDefault by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val accountTypes = listOf("Savings", "Current", "Salary")
    var expanded by remember { mutableStateOf(false) }

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
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = "Add Bank Account",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Info Card
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
                                painter = painterResource(id = R.drawable.info_24px),
                                contentDescription = null,
                                tint = Color(0xFF006D40)
                            )
                            Text(
                                "Please ensure all bank details are entered correctly. This information will be used for payment processing.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF006D40)
                            )
                        }
                    }
                }

                // Form Fields
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
                            // Account Holder Name
                            OutlinedTextField(
                                value = holderName,
                                onValueChange = { if (it.matches(Regex("^[a-zA-Z ]*$"))) holderName = it },
                                label = { Text("Account Holder Name") },
                                supportingText = { Text("Enter full name as per bank records") },
                                isError = holderName.isNotEmpty() && !holderName.matches(Regex("^[a-zA-Z ]+$")),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.profile),
                                        contentDescription = null
                                    )
                                }
                            )

                            // Bank Name
                            OutlinedTextField(
                                value = bankName,
                                onValueChange = { bankName = it },
                                label = { Text("Bank Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_balance_24px),
                                        contentDescription = null
                                    )
                                }
                            )

                            // Account Number
                            OutlinedTextField(
                                value = accountNumber,
                                onValueChange = { if (it.matches(Regex("^\\d*$"))) accountNumber = it },
                                label = { Text("Account Number") },
                                supportingText = { Text("Enter 8-16 digit account number") },
                                isError = accountNumber.isNotEmpty() && !accountNumber.matches(Regex("^\\d{8,16}$")),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_balance_wallet_24px),
                                        contentDescription = null
                                    )
                                }
                            )

                            // Account Type Dropdown
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = accountType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Account Type") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.add_card_24px),
                                            contentDescription = null
                                        )
                                    }
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    accountTypes.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = {
                                                accountType = type
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // IFSC Code
                            OutlinedTextField(
                                value = ifscCode,
                                onValueChange = { ifscCode = it.uppercase() },
                                label = { Text("IFSC Code") },
                                supportingText = { Text("11 character code (e.g., ABCD0XXXXXX)") },
                                isError = ifscCode.isNotEmpty() && !ifscCode.matches(Regex("^[A-Z]{4}[0][A-Z0-9]{6}$")),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.lock_24px),
                                        contentDescription = null
                                    )
                                }
                            )

                            // Branch Name
                            OutlinedTextField(
                                value = branchName,
                                onValueChange = { branchName = it },
                                label = { Text("Branch Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pin_drop_24px),
                                        contentDescription = null
                                    )
                                }
                            )

                            // Make Default Switch
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Set as default account")
                                Switch(
                                    checked = makeDefault,
                                    onCheckedChange = { makeDefault = it }
                                )
                            }
                        }
                    }
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            if (validateInputs(holderName, bankName, accountNumber, ifscCode, branchName)) {
                                val account = BankAccountInfo(
                                    accountNumber = accountNumber,
                                    accountType = accountType,
                                    bankName = bankName,
                                    holderName = holderName,
                                    ifscCode = ifscCode,
                                    branchName = branchName,
                                    isDefault = makeDefault
                                )
                                BankAccountManager.addAccount(account)
                                showSuccessDialog = true
                            } else {
                                hasError = true
                                errorMessage = "Please fill all the required fields correctly"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006D40)
                        )
                    ) {
                        Text("Add Bank Account")
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Dialogs
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    navController.navigateUp()
                },
                title = { Text("Success") },
                text = { Text("Bank account added successfully") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigateUp()
                        }
                    ) {
                        Text("OK")
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = null,
                        tint = Color(0xFF006D40)
                    )
                }
            )
        }

        if (hasError) {
            AlertDialog(
                onDismissRequest = { hasError = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { hasError = false }) {
                        Text("OK")
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            )
        }
    }
}

private fun validateInputs(
    holderName: String,
    bankName: String,
    accountNumber: String,
    ifscCode: String,
    branchName: String
): Boolean {
    return when {
        // Account Holder Name validation
        holderName.length < 2 || !holderName.matches(Regex("^[a-zA-Z ]+$")) -> {
            false // Name should be at least 2 characters and contain only letters
        }
        // Bank Name validation
        bankName.length < 2 -> {
            false // Bank name should be at least 2 characters
        }
        // Account Number validation
        !accountNumber.matches(Regex("^\\d{8,16}$")) -> {
            false // Account number should be 8-16 digits
        }
        // IFSC Code validation
        !ifscCode.matches(Regex("^[A-Z]{4}[0][A-Z0-9]{6}$")) -> {
            false // IFSC should be in format: ABCD0XXXXXX (11 characters)
        }
        // Branch Name validation
        branchName.length < 2 -> {
            false // Branch name should be at least 2 characters
        }
        else -> true
    }
}