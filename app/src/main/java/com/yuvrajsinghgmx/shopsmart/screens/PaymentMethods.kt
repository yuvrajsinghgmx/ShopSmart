package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Payment Methods",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Sample Payment Methods List
            Text(
                text = "Available Payment Methods:",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF332D25),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Dummy payment methods
            val paymentMethods = listOf("Credit Card", "Debit Card", "PayPal", "Google Pay")

            paymentMethods.forEach { method ->
                PaymentMethodItem(method)
            }

            Spacer(modifier = Modifier.weight(1f)) // To push content to the top
            AddPaymentMethodButton()
        }
    }
}

@Composable
fun PaymentMethodItem(method: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Text(
            text = method,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun AddPaymentMethodButton() {
    Button(
        onClick = { /* Handle adding a new payment method */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Add New Payment Method")
    }
}
