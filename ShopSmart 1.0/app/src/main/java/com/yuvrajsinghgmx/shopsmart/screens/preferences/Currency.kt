package com.yuvrajsinghgmx.shopsmart.screens.preferences

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class Currency(
    val code: String,
    val name: String,
    val symbol: String
)

class CurrencyPreferences(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "currency_prefs"
        private const val KEY_SELECTED_CURRENCY = "selected_currency"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSelectedCurrency(currencyCode: String) {
        prefs.edit().putString(KEY_SELECTED_CURRENCY, currencyCode).apply()
    }

    fun getSelectedCurrency(): String {
        return prefs.getString(KEY_SELECTED_CURRENCY, "USD") ?: "USD"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(navController: NavController) {
    val context = LocalContext.current
    val currencyPrefs = remember { CurrencyPreferences(context) }
    var selectedCurrency by remember { mutableStateOf(currencyPrefs.getSelectedCurrency()) }

    val currencies = remember {
        listOf(
            Currency("USD", "US Dollar", "$"),
            Currency("EUR", "Euro", "€"),
            Currency("GBP", "British Pound", "£"),
            Currency("INR", "Indian Rupee", "₹"),
            Currency("JPY", "Japanese Yen", "¥"),
            Currency("AUD", "Australian Dollar", "A$"),
            Currency("CAD", "Canadian Dollar", "C$"),
            Currency("CNY", "Chinese Yuan", "¥"),
            Currency("CHF", "Swiss Franc", "Fr"),
            Currency("AED", "UAE Dirham", "د.إ")
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

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
                        text = "Currency",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        "Select Currency",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(currencies) { currency ->
                    CurrencyItem(
                        currency = currency,
                        isSelected = selectedCurrency == currency.code,
                        onSelect = {
                            selectedCurrency = currency.code
                            currencyPrefs.setSelectedCurrency(currency.code)
                        }
                    )
                }

                // Add bottom spacing for navigation bar
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.currency_24px),
                    contentDescription = null,
                    tint = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Text(
                        text = currency.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currency.code,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = currency.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}