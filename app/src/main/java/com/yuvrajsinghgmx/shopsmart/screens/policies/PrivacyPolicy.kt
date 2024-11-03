package com.yuvrajsinghgmx.shopsmart.screens.policies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class PrivacySection(
    val title: String,
    val content: String,
    val iconResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)

    val privacySections = remember {
        listOf(
            PrivacySection(
                "Information We Collect",
                "We collect information that you provide directly to us, including name, email" +
                        " address, phone number, delivery address, and payment information. We also automatically" +
                        " collect certain information about your device and how you interact with our app.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "How We Use Your Information",
                "We use the information we collect to process your orders, maintain your account," +
                        " send you notifications about your purchases, personalize your shopping experience," +
                        " and improve our services. We may also use your information to communicate with you" +
                        " about promotions, updates, and other news about products and services.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Information Sharing",
                "We may share your information with third-party service providers who assist us in" +
                        " operating our app, conducting our business, or serving our users. These providers" +
                        " have access to your information only to perform specific tasks on our behalf and" +
                        " are obligated to protect your information.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Data Security",
                "We implement appropriate technical and organizational security measures to protect" +
                        " your personal information against unauthorized access, alteration, disclosure, or" +
                        " destruction. However, no method of transmission over the internet is 100% secure.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Your Choices",
                "You can review, update, or delete your account information at any time. You can" +
                        " also opt-out of receiving promotional emails and push notifications. However," +
                        " we may still send you service-related communications.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Cookies and Tracking",
                "We use cookies and similar tracking technologies to track activity on our app and" +
                        " hold certain information. You can instruct your browser to refuse all cookies or" +
                        " to indicate when a cookie is being sent.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Children's Privacy",
                "Our app is not intended for children under 13. We do not knowingly collect personal" +
                        " information from children under 13. If you believe we might have any information" +
                        " from a child under 13, please contact us.",
                R.drawable.privacy_24px
            ),
            PrivacySection(
                "Changes to Privacy Policy",
                "We may update our Privacy Policy from time to time. We will notify you of any" +
                        " changes by posting the new Privacy Policy on this page and updating the" +
                        " 'Last Updated' date.",
                R.drawable.privacy_24px
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Privacy Policy",
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
                .padding(innerPadding)
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
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Last Updated: October 28, 2024",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your privacy is important to us. This Privacy Policy explains how" +
                                    " we collect, use, and protect your personal information.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(privacySections) { section ->
                PrivacySectionCard(section)
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
                            text = "Contact Us",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "If you have any questions about this Privacy Policy, please contact us at:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "privacy@shopsmart.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Bottom Spacing
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacySectionCard(section: PrivacySection) {
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