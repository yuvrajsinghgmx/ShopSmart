package com.yuvrajsinghgmx.shopsmart.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.math.absoluteValue

data class CategorySpending(
    val category: String,
    val amount: Double,
    val percentage: Double,
    val trend: Double // Positive means increase, negative means decrease
)

data class MonthlySpending(
    val month: Month,
    val amount: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingAnalyticsScreen(navController: NavController) {
    val currentMonth = Month.NOVEMBER // Example month

    // Sample data
    val categorySpending = remember {
        listOf(
            CategorySpending("Shopping", 15000.0, 35.0, 5.2),
            CategorySpending("Food & Dining", 8000.0, 20.0, -2.1),
            CategorySpending("Bills & Utilities", 6000.0, 15.0, 0.0),
            CategorySpending("Transportation", 4000.0, 10.0, 1.5),
            CategorySpending("Entertainment", 3000.0, 8.0, -1.8),
            CategorySpending("Others", 4000.0, 12.0, 3.2)
        )
    }

    val monthlySpending = remember {
        listOf(
            MonthlySpending(Month.NOVEMBER, 40000.0),
            MonthlySpending(Month.OCTOBER, 38000.0),
            MonthlySpending(Month.SEPTEMBER, 42000.0)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5F3))
    ){

        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Spending Analytics",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF332D25)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_24px),
                        contentDescription = "Back",
                        tint = Color(0xFF332D25)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFF6F5F3)
            )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Monthly Overview Card
            item {
                MonthlyOverviewCard(monthlySpending)
            }

            // Category Breakdown Section
            item {
                Text(
                    "Category Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332D25)
                )
            }

            items(categorySpending) { category ->
                CategorySpendingCard(category)
            }

            // Insights Card
            item {
                InsightsCard(categorySpending)
            }
        }
    }
}

@Composable
private fun MonthlyOverviewCard(spending: List<MonthlySpending>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Monthly Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF006D40)
            )

            spending.forEachIndexed { index, monthSpending ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        monthSpending.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF006D40)
                    )
                    Text(
                        "₹${String.format("%,.2f", monthSpending.amount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF006D40)
                    )
                }

                if (index < spending.size - 1) {
                    val difference = spending[index].amount - spending[index + 1].amount
                    val percentageChange = (difference / spending[index + 1].amount) * 100

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (difference >= 0)
                                    R.drawable.trending_up_24px
                                else
                                    R.drawable.trending_down_24px
                            ),
                            contentDescription = null,
                            tint = if (difference >= 0) Color(0xFFB25E02) else Color(0xFF006D40),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${String.format("%.1f", percentageChange.absoluteValue)}% " +
                                    if (difference >= 0) "increase" else "decrease",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (difference >= 0) Color(0xFFB25E02) else Color(0xFF006D40)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySpendingCard(category: CategorySpending) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    category.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "₹${String.format("%,.2f", category.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LinearProgressIndicator(
                progress = category.percentage.toFloat() / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF006D40),
                trackColor = Color(0xFFE5E7EB)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${String.format("%.1f", category.percentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )

                if (category.trend != 0.0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (category.trend > 0)
                                    R.drawable.trending_up_24px
                                else
                                    R.drawable.trending_down_24px
                            ),
                            contentDescription = null,
                            tint = if (category.trend > 0) Color(0xFFB25E02) else Color(0xFF006D40),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${String.format("%.1f", category.trend.absoluteValue)}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (category.trend > 0) Color(0xFFB25E02) else Color(0xFF006D40)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightsCard(categories: List<CategorySpending>) {
    val highestCategory = categories.maxByOrNull { it.amount }
    val increasedCategories = categories.count { it.trend > 0 }

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
                painter = painterResource(id = R.drawable.lightbulb_24px),
                contentDescription = null,
                tint = Color(0xFFB25E02)
            )
            Column {
                Text(
                    "Spending Insights",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB25E02)
                )
                Spacer(modifier = Modifier.height(4.dp))
                highestCategory?.let {
                    Text(
                        "Highest spending in ${it.category} category (${it.percentage}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB25E02)
                    )
                }
                Text(
                    "$increasedCategories categories showed increased spending",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB25E02)
                )
            }
        }
    }
}