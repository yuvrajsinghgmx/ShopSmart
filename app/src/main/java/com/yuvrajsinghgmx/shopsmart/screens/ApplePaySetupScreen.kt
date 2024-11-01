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
fun ApplePaySetupScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var setupComplete by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var deviceSupported by remember { mutableStateOf(true) } // For device compatibility check

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Set up Apple Pay",
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
            // Apple Pay Logo
            item {
                Image(
                    painter = painterResource(id = R.drawable.apple_pay_24px),
                    contentDescription = "Apple Pay Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
            }

            // Device Compatibility Check
            item {
                DeviceCompatibilityCard(deviceSupported)
            }

            // Setup Status
            item {
                SetupStatusCard(setupComplete, showError, deviceSupported)
            }

            // Features Section
            item {
                FeaturesSection()
            }

            // Setup Steps
            item {
                SetupStepsCard()
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
                            if (deviceSupported) {
                                isLoading = true
                                // Simulate Apple Pay setup process
                                kotlinx.coroutines.GlobalScope.launch {
                                    kotlinx.coroutines.delay(2000)
                                    isLoading = false
                                    setupComplete = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && !setupComplete && deviceSupported,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF000000) // Apple Pay black
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(if (setupComplete) "Setup Complete" else "Set up Apple Pay")
                        }
                    }

                    if (setupComplete) {
                        OutlinedButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF000000)
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
private fun DeviceCompatibilityCard(isSupported: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = if (isSupported) Color(0xFFE7F5EC) else Color(0xFFFDEDED)
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
                    id = if (isSupported) R.drawable.ic_check_circle else R.drawable.ic_error
                ),
                contentDescription = null,
                tint = if (isSupported) Color(0xFF006D40) else Color.Red
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSupported) "Device Compatible" else "Device Not Compatible",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isSupported)
                        "Your device supports Apple Pay"
                    else
                        "Apple Pay requires an Apple device with iOS 10 or later",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}

@Composable
private fun SetupStatusCard(setupComplete: Boolean, showError: Boolean, deviceSupported: Boolean) {
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
                        setupComplete -> "Apple Pay Connected"
                        showError -> "Setup Failed"
                        !deviceSupported -> "Not Available"
                        else -> "Ready to Set Up"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when {
                        setupComplete -> "Your Apple Pay is ready to use"
                        showError -> "Please try again or contact support"
                        !deviceSupported -> "Apple Pay is not supported on this device"
                        else -> "Follow the steps below to set up Apple Pay"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}

@Composable
private fun FeaturesSection() {
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
                "Features",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FeatureItem(
                    icon = R.drawable.ic_quick_payment,
                    title = "Quick Checkout",
                    description = "Pay with a single tap using Face ID or Touch ID"
                )
                FeatureItem(
                    icon = R.drawable.ic_secure_payment,
                    title = "Secure Payments",
                    description = "Your card information is never stored or shared"
                )
                FeatureItem(
                    icon = R.drawable.ic_contactless,
                    title = "Contactless Payments",
                    description = "Pay in stores with your iPhone or Apple Watch"
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(icon: Int, title: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color(0xFF000000),
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
private fun SetupStepsCard() {
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
                "Setup Steps",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SetupStep(1, "Verify device compatibility")
                SetupStep(2, "Add a credit or debit card")
                SetupStep(3, "Verify your card details")
                SetupStep(4, "Complete authentication")
            }
        }
    }
}

@Composable
private fun SetupStep(number: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = Color(0xFFE7E7E7)
        ) {
            Text(
                text = number.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.Black,
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
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = null,
                tint = Color.Black
            )
            Column {
                Text(
                    "Enhanced Security",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    "Apple Pay uses industry-leading security and dedicated chips for payment protection",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
        }
    }
}