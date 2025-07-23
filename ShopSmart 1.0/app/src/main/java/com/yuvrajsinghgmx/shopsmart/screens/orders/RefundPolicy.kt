package com.yuvrajsinghgmx.shopsmart.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundPolicyScreen(navController: NavController) {
    val backgroundColor = Color(0xFFF6F5F3)

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF332D25)
                        )
                    }

                    Text(
                        text = "Refund Policy",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // General Policy Section
                item {
                    PolicySection(
                        title = "General Policy",
                        content = listOf(
                            "We accept returns within 30 days of purchase",
                            "Items must be unused and in original packaging",
                            "Return shipping costs may apply",
                            "Refunds are processed within 5-7 business days"
                        )
                    )
                }

                // Eligible Items Section
                item {
                    PolicySection(
                        title = "Eligible Items",
                        content = listOf(
                            "Clothing and accessories in original condition",
                            "Unopened beauty products",
                            "Electronics with all original accessories",
                            "Furniture without assembly or use"
                        )
                    )
                }

                // Non-Eligible Items Section
                item {
                    PolicySection(
                        title = "Non-Eligible Items",
                        content = listOf(
                            "Personalized or custom-made items",
                            "Intimate apparel and swimwear",
                            "Used or damaged items",
                            "Digital products and gift cards"
                        )
                    )
                }

                // Refund Process Section
                item {
                    PolicySection(
                        title = "Refund Process",
                        content = listOf(
                            "1. Initiate return through your account",
                            "2. Print return shipping label",
                            "3. Pack items securely with original packaging",
                            "4. Drop off at designated shipping locations",
                            "5. Refund issued after quality check"
                        )
                    )
                }

                // Payment Methods Section
                item {
                    PolicySection(
                        title = "Refund Methods",
                        content = listOf(
                            "Original payment method (default)",
                            "Store credit (processed faster)",
                            "Bank account transfer",
                            "Digital wallet credit"
                        )
                    )
                }

                // Additional Information
                item {
                    PolicySection(
                        title = "Additional Information",
                        content = listOf(
                            "Sale items may have different return policies",
                            "International orders may have longer processing times",
                            "Bulk orders require special return authorization",
                            "Seasonal items may have specific return windows"
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    content: List<String>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF332D25)
            )

            content.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "•",
                        color = Color(0xFF637478)
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF637478),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}