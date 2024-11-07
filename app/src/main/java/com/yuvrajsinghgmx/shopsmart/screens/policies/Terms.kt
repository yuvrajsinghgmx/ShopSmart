package com.yuvrajsinghgmx.shopsmart.screens.policies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class TermsSection(
    val title: String,
    val content: String,
    val iconResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)

    val termsSections = remember {
        listOf(
            TermsSection(
                "Acceptance of Terms",
                "By accessing and using ShopSmart, you agree to be bound by these Terms and Conditions" +
                        " and all applicable laws and regulations. If you do not agree with any of these terms," +
                        " you are prohibited from using or accessing this app.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "User Account",
                "Users must register for an account to access certain features. You are responsible" +
                        " for maintaining the confidentiality of your account and password. You agree to" +
                        " accept responsibility for all activities that occur under your account.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Shopping & Purchases",
                "All purchases through our app are subject to product availability. We reserve the" +
                        " right to refuse service, modify product prices, and cancel orders at our sole" +
                        " discretion.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Payment Terms",
                "We accept various payment methods. All payment information is encrypted and" +
                        " securely processed. By making a purchase, you warrant that you have the legal" +
                        " right to use the payment method provided.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Shipping & Delivery",
                "Shipping times and costs vary by location and method. We are not responsible for" +
                        " delays caused by customs, weather, or other factors beyond our control.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Returns & Refunds",
                "Products may be returned within 30 days of delivery. Items must be unused and in" +
                        " original packaging. Refunds will be processed to the original payment method.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "User Content",
                "By posting content on our platform, you grant us a non-exclusive right to use," +
                        " modify, reproduce, and distribute that content. You are solely responsible for" +
                        " your content.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Intellectual Property",
                "All content, trademarks, and other intellectual property on this app are owned" +
                        " by ShopSmart. You may not use our intellectual property without explicit" +
                        " permission.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Limitation of Liability",
                "ShopSmart shall not be liable for any indirect, incidental, special, or" +
                        " consequential damages resulting from the use or inability to use our services.",
                R.drawable.terms_24px
            ),
            TermsSection(
                "Changes to Terms",
                "We reserve the right to modify these terms at any time. Users will be notified" +
                        " of significant changes. Continued use of the app constitutes acceptance of" +
                        " modified terms.",
                R.drawable.terms_24px
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = lightBackgroundColor,
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = "Terms & Conditions",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Last Updated Section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Last Updated: October 28, 2024",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Please read these terms carefully before using the app",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(termsSections) { section ->
                    TermsSectionCard(section)
                }

                item {
                    // Contact Information
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Questions about the Terms?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Contact us at: support@shopsmart.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Bottom Spacing for navigation bar
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsSectionCard(section: TermsSection) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = section.iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}