package com.yuvrajsinghgmx.shopsmart.screens.support

import androidx.compose.foundation.background
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

data class ContactOption(
    val title: String,
    val subtitle: String,
    val route: String,
    val icon: Int
)

data class CommonIssue(
    val title: String,
    val subtitle: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(navController: NavController) {
    val backgroundColor = Color(0xFFF6F5F3)

    val contactOptions = remember {
        listOf(
            ContactOption(
                "Live Chat",
                "Chat with our support team",
                "live_chat",
                R.drawable.chat_24px
            ),
            ContactOption(
                "Email Support",
                "Send us an email",
                "email_support",
                R.drawable.chat_24px
            ),
            ContactOption(
                "Call Us",
                "Speak with a representative",
                "phone_support",
                R.drawable.contact_phone_24px
            )
        )
    }

    val commonIssues = remember {
        listOf(
            CommonIssue(
                "Track Refund Status",
                "Check the status of your refund request",
                "refund_status"
            ),
            CommonIssue(
                "Return Guidelines",
                "Learn about our return process",
                "return_guidelines"
            ),
            CommonIssue(
                "Missing Refund",
                "Help with missing or delayed refunds",
                "missing_refund"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = backgroundColor,
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
                        text = "Contact Support",
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        "How Can We Help?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25)
                    )
                }

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column {
                            contactOptions.forEachIndexed { index, option ->
                                ContactOptionItem(
                                    option = option,
                                    onClick = { navController.navigate(option.route) }
                                )
                                if (index < contactOptions.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color(0xFFE5E7EB)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Common Issues",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25)
                    )
                }

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column {
                            commonIssues.forEachIndexed { index, issue ->
                                CommonIssueItem(
                                    issue = issue,
                                    onClick = { navController.navigate(issue.route) }
                                )
                                if (index < commonIssues.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color(0xFFE5E7EB)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    SupportHoursCard()
                }
            }
        }
    }
}

@Composable
private fun ContactOptionItem(
    option: ContactOption,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = option.icon),
                contentDescription = null,
                tint = Color(0xFF006D40)
            )
            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF332D25)
                )
                Text(
                    text = option.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.arrow_forward_24px),
            contentDescription = "Navigate",
            tint = Color(0xFF637478)
        )
    }
}

@Composable
private fun CommonIssueItem(
    issue: CommonIssue,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = issue.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF332D25)
            )
            Text(
                text = issue.subtitle,
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

@Composable
private fun SupportHoursCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Support Hours",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF332D25)
            )
            Text(
                text = "Monday - Friday: 9:00 AM - 8:00 PM",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
            Text(
                text = "Saturday: 10:00 AM - 6:00 PM",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
            Text(
                text = "Sunday: Closed",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
        }
    }
}