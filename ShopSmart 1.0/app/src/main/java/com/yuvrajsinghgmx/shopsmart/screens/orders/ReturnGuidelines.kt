package com.yuvrajsinghgmx.shopsmart.screens.orders

import androidx.compose.animation.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class ReturnPolicy(
    val title: String,
    val content: List<String>,
    val icon: Int
)

data class ReturnCategory(
    val title: String,
    val subtitle: String,
    val returnWindow: String,
    val conditions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnGuidelinesScreen(navController: NavController) {
    var expandedSection by remember { mutableStateOf<String?>(null) }

    val returnPolicies = listOf(
        ReturnPolicy(
            "Return Window",
            listOf(
                "Most items can be returned within 30 days of delivery",
                "Some categories have different return windows",
                "Return window starts from the date of delivery"
            ),
            R.drawable.schedule_24px
        ),
        ReturnPolicy(
            "Return Condition",
            listOf(
                "Items must be unused and in original condition",
                "All original tags and packaging must be intact",
                "Include all accessories and free gifts received",
                "Items should not be damaged or altered"
            ),
            R.drawable.inventory_24px
        ),
        ReturnPolicy(
            "Non-Returnable Items",
            listOf(
                "Customized or personalized items",
                "Perishable goods and groceries",
                "Intimate wear and swimwear",
                "Digital downloads and gift cards",
                "Items marked as non-returnable"
            ),
            R.drawable.block_24px
        )
    )

    val returnCategories = listOf(
        ReturnCategory(
            "Electronics",
            "Mobiles, Laptops, Accessories",
            "7 days",
            listOf(
                "Product should be in original condition",
                "All accessories must be returned",
                "IMEI/Serial numbers should match",
                "No physical damage or scratches"
            )
        ),
        ReturnCategory(
            "Fashion",
            "Clothing, Shoes, Accessories",
            "30 days",
            listOf(
                "Unworn with original tags attached",
                "No signs of use or alteration",
                "Original packaging intact",
                "No stains or damages"
            )
        ),
        ReturnCategory(
            "Home & Living",
            "Furniture, Decor, Kitchen",
            "10 days",
            listOf(
                "Unassembled condition if delivered so",
                "No scratches or damages",
                "All parts and fittings included",
                "Original packaging preferred"
            )
        )
    )

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
                        text = "Return Guidelines",
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
                // General Return Process
                item {
                    ReturnProcessCard()
                }

                // Return Policies
                items(returnPolicies) { policy ->
                    PolicyCard(policy)
                }

                // Category Specific Guidelines
                item {
                    Text(
                        "Category Guidelines",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25)
                    )
                }

                items(returnCategories) { category ->
                    CategoryGuidelineCard(
                        category = category,
                        isExpanded = expandedSection == category.title,
                        onExpandClick = {
                            expandedSection = if (expandedSection == category.title) null else category.title
                        }
                    )
                }

                // Return Tips
                item {
                    ReturnTipsCard()
                }
            }
        }
    }
}

@Composable
private fun ReturnProcessCard() {
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
                "Return Process",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            val steps = listOf(
                "Initiate Return" to "Go to My Orders and select the item to return",
                "Package Item" to "Pack the item securely in original packaging",
                "Pickup/Drop-off" to "Choose pickup or drop at nearest center",
                "Refund" to "Refund processed after quality check"
            )

            steps.forEachIndexed { index, (title, description) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color(0xFF006D40)
                    ) {
                        Text(
                            "${index + 1}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Column {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF637478)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicyCard(policy: ReturnPolicy) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = policy.icon),
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
                Text(
                    policy.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            policy.content.forEach { point ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = Color(0xFF637478))
                    Text(
                        point,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryGuidelineCard(
    category: ReturnCategory,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        onClick = onExpandClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        category.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        category.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        "Return Window: ${category.returnWindow}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF006D40),
                        fontWeight = FontWeight.Medium
                    )
                }
                IconButton(onClick = onExpandClick) {
                    Icon(
                        painter = painterResource(
                            id = if (isExpanded)
                                R.drawable.expand_less_24px
                            else
                                R.drawable.expand_more_24px
                        ),
                        contentDescription = if (isExpanded) "Show less" else "Show more",
                        tint = Color(0xFF637478)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Return Conditions:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    category.conditions.forEach { condition ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("•", color = Color(0xFF637478))
                            Text(
                                condition,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF637478),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReturnTipsCard() {
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
                painter = painterResource(id = R.drawable.tips_and_updates_24px),
                contentDescription = null,
                tint = Color(0xFF006D40)
            )
            Column {
                Text(
                    "Return Tips",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF006D40)
                )
                Spacer(modifier = Modifier.height(4.dp))
                val tips = listOf(
                    "Take photos before returning",
                    "Keep all original packaging",
                    "Document any defects",
                    "Save return shipping label"
                )
                tips.forEach { tip ->
                    Text(
                        "• $tip",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF006D40)
                    )
                }
            }
        }
    }
}

