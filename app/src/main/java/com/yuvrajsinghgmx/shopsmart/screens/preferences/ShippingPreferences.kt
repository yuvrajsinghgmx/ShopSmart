package com.yuvrajsinghgmx.shopsmart.screens.preferences

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

class ShippingPreferences(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "shipping_prefs"
        private const val KEY_DEFAULT_SHIPPING = "default_shipping"
        private const val KEY_EXPRESS_DELIVERY = "express_delivery"
        private const val KEY_CONTACT_BEFORE_DELIVERY = "contact_before_delivery"
        private const val KEY_WEEKEND_DELIVERY = "weekend_delivery"
        private const val KEY_DELIVERY_INSTRUCTIONS = "delivery_instructions"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setDefaultShipping(method: String) {
        prefs.edit().putString(KEY_DEFAULT_SHIPPING, method).apply()
    }

    fun getDefaultShipping(): String {
        return prefs.getString(KEY_DEFAULT_SHIPPING, "standard") ?: "standard"
    }

    fun setExpressDelivery(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_EXPRESS_DELIVERY, enabled).apply()
    }

    fun getExpressDelivery(): Boolean = prefs.getBoolean(KEY_EXPRESS_DELIVERY, false)

    fun setContactBeforeDelivery(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CONTACT_BEFORE_DELIVERY, enabled).apply()
    }

    fun getContactBeforeDelivery(): Boolean = prefs.getBoolean(KEY_CONTACT_BEFORE_DELIVERY, true)

    fun setWeekendDelivery(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_WEEKEND_DELIVERY, enabled).apply()
    }

    fun getWeekendDelivery(): Boolean = prefs.getBoolean(KEY_WEEKEND_DELIVERY, false)

    fun setDeliveryInstructions(instructions: String) {
        prefs.edit().putString(KEY_DELIVERY_INSTRUCTIONS, instructions).apply()
    }

    fun getDeliveryInstructions(): String {
        return prefs.getString(KEY_DELIVERY_INSTRUCTIONS, "") ?: ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingPreferencesScreen(navController: NavController) {
    val context = LocalContext.current
    val shippingPrefs = remember { ShippingPreferences(context) }

    var defaultShipping by remember { mutableStateOf(shippingPrefs.getDefaultShipping()) }
    var expressDelivery by remember { mutableStateOf(shippingPrefs.getExpressDelivery()) }
    var contactBeforeDelivery by remember { mutableStateOf(shippingPrefs.getContactBeforeDelivery()) }
    var weekendDelivery by remember { mutableStateOf(shippingPrefs.getWeekendDelivery()) }
    var deliveryInstructions by remember { mutableStateOf(shippingPrefs.getDeliveryInstructions()) }

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
                        text = "Shipping Preferences",
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
                    // Default Shipping Method Section
                    SectionTitle("Default Shipping Method")

                    ShippingMethodCard(
                        title = "Standard Delivery",
                        subtitle = "5-7 business days",
                        iconResId = R.drawable.shipping_24px,
                        isSelected = defaultShipping == "standard",
                        onClick = {
                            defaultShipping = "standard"
                            shippingPrefs.setDefaultShipping("standard")
                        }
                    )

                    ShippingMethodCard(
                        title = "Express Delivery",
                        subtitle = "2-3 business days",
                        iconResId = R.drawable.shipping_24px,
                        isSelected = defaultShipping == "express",
                        onClick = {
                            defaultShipping = "express"
                            shippingPrefs.setDefaultShipping("express")
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    // Delivery Preferences Section
                    SectionTitle("Delivery Preferences")

                    PreferenceToggleItem(
                        title = "Express Delivery Available",
                        subtitle = "Show express delivery options when available",
                        iconResId = R.drawable.shipping_24px,
                        checked = expressDelivery,
                        onCheckedChange = {
                            expressDelivery = it
                            shippingPrefs.setExpressDelivery(it)
                        }
                    )

                    PreferenceToggleItem(
                        title = "Contact Before Delivery",
                        subtitle = "Delivery person will contact you before delivery",
                        iconResId = R.drawable.shipping_24px,
                        checked = contactBeforeDelivery,
                        onCheckedChange = {
                            contactBeforeDelivery = it
                            shippingPrefs.setContactBeforeDelivery(it)
                        }
                    )

                    PreferenceToggleItem(
                        title = "Weekend Delivery",
                        subtitle = "Allow deliveries on weekends",
                        iconResId = R.drawable.shipping_24px,
                        checked = weekendDelivery,
                        onCheckedChange = {
                            weekendDelivery = it
                            shippingPrefs.setWeekendDelivery(it)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    // Delivery Instructions Section
                    SectionTitle("Delivery Instructions")

                    OutlinedTextField(
                        value = deliveryInstructions,
                        onValueChange = {
                            deliveryInstructions = it
                            shippingPrefs.setDeliveryInstructions(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        label = { Text("Special Instructions for Delivery") },
                        placeholder = { Text("E.g., Leave at the front door") },
                        minLines = 3,
                        maxLines = 5
                    )

                    // Bottom spacing for navigation bar
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShippingMethodCard(
    title: String,
    subtitle: String,
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick,
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
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            if (isSelected) {
                RadioButton(
                    selected = true,
                    onClick = null
                )
            }
        }
    }
}

@Composable
private fun PreferenceToggleItem(
    title: String,
    subtitle: String,
    iconResId: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}