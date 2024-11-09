package com.yuvrajsinghgmx.shopsmart.screens.orders

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

class OrderNotificationPreferences(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "order_notification_prefs"
        private const val KEY_ORDER_CONFIRMATION = "order_confirmation"
        private const val KEY_SHIPPING_UPDATES = "shipping_updates"
        private const val KEY_DELIVERY_UPDATES = "delivery_updates"
        private const val KEY_ORDER_CANCELLATION = "order_cancellation"
        private const val KEY_PRICE_DROPS = "price_drops"
        private const val KEY_BACK_IN_STOCK = "back_in_stock"
        private const val KEY_EMAIL_NOTIFICATIONS = "email_notifications"
        private const val KEY_PUSH_NOTIFICATIONS = "push_notifications"
        private const val KEY_SMS_NOTIFICATIONS = "sms_notifications"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setOrderConfirmation(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ORDER_CONFIRMATION, enabled).apply()
    }

    fun getOrderConfirmation(): Boolean = prefs.getBoolean(KEY_ORDER_CONFIRMATION, true)

    fun setShippingUpdates(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHIPPING_UPDATES, enabled).apply()
    }

    fun getShippingUpdates(): Boolean = prefs.getBoolean(KEY_SHIPPING_UPDATES, true)

    fun setDeliveryUpdates(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DELIVERY_UPDATES, enabled).apply()
    }

    fun getDeliveryUpdates(): Boolean = prefs.getBoolean(KEY_DELIVERY_UPDATES, true)

    fun setOrderCancellation(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ORDER_CANCELLATION, enabled).apply()
    }

    fun getOrderCancellation(): Boolean = prefs.getBoolean(KEY_ORDER_CANCELLATION, true)

    fun setPriceDrops(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PRICE_DROPS, enabled).apply()
    }

    fun getPriceDrops(): Boolean = prefs.getBoolean(KEY_PRICE_DROPS, true)

    fun setBackInStock(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BACK_IN_STOCK, enabled).apply()
    }

    fun getBackInStock(): Boolean = prefs.getBoolean(KEY_BACK_IN_STOCK, true)

    fun setEmailNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_EMAIL_NOTIFICATIONS, enabled).apply()
    }

    fun getEmailNotifications(): Boolean = prefs.getBoolean(KEY_EMAIL_NOTIFICATIONS, true)

    fun setPushNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PUSH_NOTIFICATIONS, enabled).apply()
    }

    fun getPushNotifications(): Boolean = prefs.getBoolean(KEY_PUSH_NOTIFICATIONS, true)

    fun setSMSNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SMS_NOTIFICATIONS, enabled).apply()
    }

    fun getSMSNotifications(): Boolean = prefs.getBoolean(KEY_SMS_NOTIFICATIONS, false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderNotificationsScreen(navController: NavController) {
    val context = LocalContext.current
    val notificationPrefs = remember { OrderNotificationPreferences(context) }

    var orderConfirmation by remember { mutableStateOf(notificationPrefs.getOrderConfirmation()) }
    var shippingUpdates by remember { mutableStateOf(notificationPrefs.getShippingUpdates()) }
    var deliveryUpdates by remember { mutableStateOf(notificationPrefs.getDeliveryUpdates()) }
    var orderCancellation by remember { mutableStateOf(notificationPrefs.getOrderCancellation()) }
    var priceDrops by remember { mutableStateOf(notificationPrefs.getPriceDrops()) }
    var backInStock by remember { mutableStateOf(notificationPrefs.getBackInStock()) }
    var emailNotifications by remember { mutableStateOf(notificationPrefs.getEmailNotifications()) }
    var pushNotifications by remember { mutableStateOf(notificationPrefs.getPushNotifications()) }
    var smsNotifications by remember { mutableStateOf(notificationPrefs.getSMSNotifications()) }

    val lightBackgroundColor = Color(0xFFF6F5F3)

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
                        text = "Order Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Order Status Section
                    SectionTitle("Order Status Updates")

                    NotificationToggleItem(
                        title = "Order Confirmation",
                        subtitle = "Receive notifications when your order is confirmed",
                        iconResId = R.drawable.notifications_24px,
                        checked = orderConfirmation,
                        onCheckedChange = {
                            orderConfirmation = it
                            notificationPrefs.setOrderConfirmation(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "Shipping Updates",
                        subtitle = "Get notified about shipping status changes",
                        iconResId = R.drawable.notifications_24px,
                        checked = shippingUpdates,
                        onCheckedChange = {
                            shippingUpdates = it
                            notificationPrefs.setShippingUpdates(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "Delivery Updates",
                        subtitle = "Receive notifications about delivery status",
                        iconResId = R.drawable.notifications_24px,
                        checked = deliveryUpdates,
                        onCheckedChange = {
                            deliveryUpdates = it
                            notificationPrefs.setDeliveryUpdates(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "Order Cancellation",
                        subtitle = "Get notified about order cancellations",
                        iconResId = R.drawable.notifications_24px,
                        checked = orderCancellation,
                        onCheckedChange = {
                            orderCancellation = it
                            notificationPrefs.setOrderCancellation(it)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    // Product Updates Section
                    SectionTitle("Product Updates")

                    NotificationToggleItem(
                        title = "Price Drops",
                        subtitle = "Get notified when prices drop on your wishlist items",
                        iconResId = R.drawable.notifications_24px,
                        checked = priceDrops,
                        onCheckedChange = {
                            priceDrops = it
                            notificationPrefs.setPriceDrops(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "Back in Stock",
                        subtitle = "Receive alerts when out-of-stock items are available",
                        iconResId = R.drawable.notifications_24px,
                        checked = backInStock,
                        onCheckedChange = {
                            backInStock = it
                            notificationPrefs.setBackInStock(it)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    // Notification Methods Section
                    SectionTitle("Notification Methods")

                    NotificationToggleItem(
                        title = "Email Notifications",
                        subtitle = "Receive notifications via email",
                        iconResId = R.drawable.notifications_24px,
                        checked = emailNotifications,
                        onCheckedChange = {
                            emailNotifications = it
                            notificationPrefs.setEmailNotifications(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "Push Notifications",
                        subtitle = "Receive notifications on your device",
                        iconResId = R.drawable.notifications_24px,
                        checked = pushNotifications,
                        onCheckedChange = {
                            pushNotifications = it
                            notificationPrefs.setPushNotifications(it)
                        }
                    )

                    NotificationToggleItem(
                        title = "SMS Notifications",
                        subtitle = "Receive notifications via SMS",
                        iconResId = R.drawable.notifications_24px,
                        checked = smsNotifications,
                        onCheckedChange = {
                            smsNotifications = it
                            notificationPrefs.setSMSNotifications(it)
                        }
                    )

                    // Bottom Spacing
                    Spacer(modifier = Modifier.height(16.dp))
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

@Composable
private fun NotificationToggleItem(
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