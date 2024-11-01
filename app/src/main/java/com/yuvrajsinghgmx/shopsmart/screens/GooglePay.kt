package com.yuvrajsinghgmx.shopsmart.screens

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
fun GooglePaySetupScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var setupComplete by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Set up Google Pay",
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
            // Google Pay Logo and Header
            item {
                Image(
                    painter = painterResource(id = R.drawable.google_pay_large),
                    contentDescription = "Google Pay Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
            }

            // Setup Status
            item {
                SetupStatusCard(setupComplete, showError)
            }

            // Benefits Section
            item {
                BenefitsSection()
            }

            // Setup Instructions
            item {
                SetupInstructionsCard()
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
                            isLoading = true
                            // Simulate setup process
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(2000)
                                isLoading = false
                                setupComplete = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && !setupComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006D40)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(if (setupComplete) "Setup Complete" else "Set up Google Pay")
                        }
                    }

                    if (setupComplete) {
                        OutlinedButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF006D40)
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
                    setupComplete -> Color(0xFF006D40)
                    showError -> Color.Red
                    else -> Color(0xFF637478)
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when {
                        setupComplete -> "Google Pay Connected"
                        showError -> "Setup Failed"
                        else -> "Ready to Set Up"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when {
                        setupComplete -> "Your Google Pay account is ready to use"
                        showError -> "Please try again or contact support"
                        else -> "Connect your Google account to enable Google Pay"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
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
                "Benefits of Google Pay",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BenefitItem(
                    title = "Fast Checkout",
                    description = "Pay with one tap using your saved cards"
                )
                BenefitItem(
                    title = "Secure Payments",
                    description = "Your payment info is encrypted and protected"
                )
                BenefitItem(
                    title = "Rewards",
                    description = "Earn points and cashback on eligible purchases"
                )
            }
        }
    }
}

@Composable
private fun BenefitItem(title: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = null,
            tint = Color(0xFF006D40),
            modifier = Modifier.size(20.dp)
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
private fun SetupInstructionsCard() {
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
                "Setup Instructions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InstructionStep(
                    number = 1,
                    text = "Sign in with your Google Account"
                )
                InstructionStep(
                    number = 2,
                    text = "Add or select a payment method"
                )
                InstructionStep(
                    number = 3,
                    text = "Verify your identity"
                )
                InstructionStep(
                    number = 4,
                    text = "Start using Google Pay"
                )
            }
        }
    }
}

@Composable
private fun InstructionStep(number: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = Color(0xFFE7F5EC)
        ) {
            Text(
                text = number.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color(0xFF006D40),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SecurityInfoCard() {
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
            Column {
                Text(
                    "Secure Payments",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB25E02)
                )
                Text(
                    "Your payment information is protected with industry-leading security",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB25E02)
                )
            }
        }
    }
}