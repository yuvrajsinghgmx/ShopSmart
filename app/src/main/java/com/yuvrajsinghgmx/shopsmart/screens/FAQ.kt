package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class FAQItem(
    val question: String,
    val answer: String,
    val category: String
)

data class FAQCategory(
    val name: String,
    val items: List<FAQItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val lightBackgroundColor = Color(0xFFF6F5F3)

    // FAQ Data
    val faqCategories = remember {
        listOf(
            FAQCategory(
                "Shopping & Orders",
                listOf(
                    FAQItem(
                        "How do I place an order?",
                        "To place an order, simply browse our products, add items to your cart, " +
                                "and proceed to checkout. Follow the steps to enter your shipping and payment information.",
                        "Shopping"
                    ),
                    FAQItem(
                        "What payment methods do you accept?",
                        "We accept various payment methods including credit/debit cards, PayPal, " +
                                "and digital wallets like Google Pay and Apple Pay.",
                        "Shopping"
                    ),
                    FAQItem(
                        "How can I track my order?",
                        "Once your order is shipped, you'll receive a tracking number via email. " +
                                "You can also track your order in the 'My Orders' section of your account.",
                        "Shopping"
                    )
                )
            ),
            FAQCategory(
                "Returns & Refunds",
                listOf(
                    FAQItem(
                        "What is your return policy?",
                        "We offer a 30-day return policy for most items. Products must be unused " +
                                "and in their original packaging with all tags attached.",
                        "Returns"
                    ),
                    FAQItem(
                        "How do I initiate a return?",
                        "To initiate a return, go to 'My Orders', select the order and item you " +
                                "want to return, and follow the return instructions. You'll receive a return " +
                                "shipping label via email.",
                        "Returns"
                    ),
                    FAQItem(
                        "When will I receive my refund?",
                        "Refunds are typically processed within 5-7 business days after we receive " +
                                "the returned item. The amount will be credited to your original payment method.",
                        "Returns"
                    )
                )
            ),
            FAQCategory(
                "Account & Security",
                listOf(
                    FAQItem(
                        "How do I reset my password?",
                        "Click on 'Forgot Password' on the login screen, enter your email address, " +
                                "and follow the instructions sent to your email to reset your password.",
                        "Account"
                    ),
                    FAQItem(
                        "Is my payment information secure?",
                        "Yes, we use industry-standard encryption to protect your payment information. " +
                                "We never store your full credit card details on our servers.",
                        "Account"
                    ),
                    FAQItem(
                        "How can I update my account information?",
                        "Go to 'Profile' and select 'Personal Information' to update your account " +
                                "details, including name, email, and phone number.",
                        "Account"
                    )
                )
            ),
            FAQCategory(
                "Shipping & Delivery",
                listOf(
                    FAQItem(
                        "What are your shipping options?",
                        "We offer standard shipping (5-7 business days) and express shipping " +
                                "(2-3 business days). Shipping costs vary based on location and selected method.",
                        "Shipping"
                    ),
                    FAQItem(
                        "Do you ship internationally?",
                        "Yes, we ship to most countries worldwide. International shipping times " +
                                "and costs vary by location. Check the shipping calculator at checkout for details.",
                        "Shipping"
                    ),
                    FAQItem(
                        "What if my package is delayed?",
                        "If your package is delayed, check the tracking information first. If there's " +
                                "a significant delay, contact our customer support with your order number.",
                        "Shipping"
                    )
                )
            ),
            FAQCategory(
                "App Features",
                listOf(
                    FAQItem(
                        "How does the wishlist work?",
                        "Add items to your wishlist by clicking the heart icon. Access your wishlist " +
                                "from your profile to track prices and availability.",
                        "Features"
                    ),
                    FAQItem(
                        "Can I save my payment information?",
                        "Yes, you can securely save payment methods in your account for faster checkout. " +
                                "Manage saved payment methods in your account settings.",
                        "Features"
                    ),
                    FAQItem(
                        "How do I enable notifications?",
                        "Go to 'Settings' > 'Notifications' to customize your notification preferences " +
                                "for orders, deals, and updates.",
                        "Features"
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "FAQ",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search FAQs") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )

            // FAQ List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                faqCategories.forEach { category ->
                    val filteredItems = category.items.filter {
                        it.question.contains(searchQuery, ignoreCase = true) ||
                                it.answer.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredItems.isNotEmpty()) {
                        item {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        items(filteredItems) { faq ->
                            FAQExpandableCard(faq)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQExpandableCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Question Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Show less" else "Show more",
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }

            // Answer
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}