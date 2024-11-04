package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.Image
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPalSetupScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var setupComplete by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Set up PayPal",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // PayPal Logo
            item {
                Image(
                    painter = painterResource(id = R.drawable.paypal_large),
                    contentDescription = "PayPal Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
            }

            // Setup Status
            item {
                SetupStatusCard(setupComplete, showError)
            }

            // Email Input Section
            item {
                EmailInputSection(
                    email = email,
                    isValid = isEmailValid,
                    onEmailChange = {
                        email = it
                        isEmailValid = it.isEmpty() || it.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
                    }
                )
            }

            // Benefits Section
            item {
                BenefitsSection()
            }

            // Security Info
            item {
                SecurityInfoCard()
            }

            // Action Buttons
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && isEmailValid) {
                                isLoading = true
                                // Simulate PayPal connection
                                kotlinx.coroutines.GlobalScope.launch {
                                    kotlinx.coroutines.delay(2000)
                                    isLoading = false
                                    setupComplete = true
                                }
                            } else {
                                isEmailValid = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && !setupComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0079C1) // PayPal blue
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(if (setupComplete) "PayPal Connected" else "Connect with PayPal")
                        }
                    }

                    if (setupComplete) {
                        OutlinedButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF0079C1)
                            )
                        ) {
                            Text("Return to Payment Methods")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupStatusCard(setupComplete: Boolean, showError: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = if (setupComplete) Color(0xFFE7F5EC) else if (showError) Color(0xFFFDEDED) else Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = when {
                        setupComplete -> R.drawable.ic_check_circle
                        showError -> R.drawable.ic_error
                        else -> R.drawable.ic_lock
                    }
                ),
                contentDescription = null,
                tint = when {
                    setupComplete -> Color(0xFF0079C1)
                    showError -> Color.Red
                    else -> Color(0xFF637478)
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when {
                        setupComplete -> "PayPal Connected"
                        showError -> "Connection Failed"
                        else -> "Connect Your PayPal Account"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when {
                        setupComplete -> "Your PayPal account is ready to use"
                        showError -> "Please try again or contact support"
                        else -> "Sign in to your PayPal account to continue"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailInputSection(
    email: String,
    isValid: Boolean,
    onEmailChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "PayPal Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isValid,
                supportingText = if (!isValid) {
                    { Text("Please enter a valid email address") }
                } else null,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun BenefitsSection() {
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
                "Benefits of PayPal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BenefitItem(
                    icon = R.drawable.ic_security,
                    title = "Buyer Protection",
                    description = "Shop with confidence and payment protection"
                )
                BenefitItem(
                    icon = R.drawable.ic_speed,
                    title = "Fast Checkout",
                    description = "Quick and easy payments with saved info"
                )
                BenefitItem(
                    icon = R.drawable.ic_global,
                    title = "Global Coverage",
                    description = "Pay in multiple currencies worldwide"
                )
            }
        }
    }
}

@Composable
private fun BenefitItem(icon: Int, title: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color(0xFF0079C1),
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF637478)
            )
        }
    }
}

@Composable
private fun SecurityInfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFF5F8FA)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = null,
                tint = Color(0xFF0079C1)
            )
            Column {
                Text(
                    "Secure Connection",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0079C1)
                )
                Text(
                    "Your PayPal connection is protected with industry-standard encryption",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}