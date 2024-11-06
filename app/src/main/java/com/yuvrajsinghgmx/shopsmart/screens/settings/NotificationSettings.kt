package com.yuvrajsinghgmx.shopsmart.screens.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

class NotificationPreferences(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "notification_prefs"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_PROMOTIONAL_ENABLED = "promotional_enabled"
        private const val KEY_ORDER_UPDATES_ENABLED = "order_updates_enabled"
        private const val KEY_DELIVERY_UPDATES_ENABLED = "delivery_updates_enabled"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun getNotificationsEnabled(): Boolean = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }

    fun getSoundEnabled(): Boolean = prefs.getBoolean(KEY_SOUND_ENABLED, true)

    fun setVibrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply()
    }

    fun getVibrationEnabled(): Boolean = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)

    fun setPromotionalEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PROMOTIONAL_ENABLED, enabled).apply()
    }

    fun getPromotionalEnabled(): Boolean = prefs.getBoolean(KEY_PROMOTIONAL_ENABLED, true)

    fun setOrderUpdatesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ORDER_UPDATES_ENABLED, enabled).apply()
    }

    fun getOrderUpdatesEnabled(): Boolean = prefs.getBoolean(KEY_ORDER_UPDATES_ENABLED, true)

    fun setDeliveryUpdatesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DELIVERY_UPDATES_ENABLED, enabled).apply()
    }

    fun getDeliveryUpdatesEnabled(): Boolean = prefs.getBoolean(KEY_DELIVERY_UPDATES_ENABLED, true)
}

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onGoToSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notification Permission Required") },
        text = { Text("To receive notifications, please enable notifications in your device settings.") },
        confirmButton = {
            TextButton(onClick = onGoToSettings) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not Now")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettings(navController: NavController) {
    val context = LocalContext.current
    val notificationPrefs = remember { NotificationPreferences(context) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    var notificationsEnabled by remember {
        mutableStateOf(notificationPrefs.getNotificationsEnabled())
    }
    var soundEnabled by remember {
        mutableStateOf(notificationPrefs.getSoundEnabled())
    }
    var vibrationEnabled by remember {
        mutableStateOf(notificationPrefs.getVibrationEnabled())
    }
    var promotionalEnabled by remember {
        mutableStateOf(notificationPrefs.getPromotionalEnabled())
    }
    var orderUpdatesEnabled by remember {
        mutableStateOf(notificationPrefs.getOrderUpdatesEnabled())
    }
    var deliveryUpdatesEnabled by remember {
        mutableStateOf(notificationPrefs.getDeliveryUpdatesEnabled())
    }

    if (showPermissionDialog) {
        NotificationPermissionDialog(
            onDismiss = { showPermissionDialog = false },
            onGoToSettings = {
                showPermissionDialog = false
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF332D25)
                    )
                }

                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF332D25),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Main Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    // Enable Notifications Toggle
                    NotificationSwitchItemWithImageVector(
                        title = "Enable Notifications",
                        subtitle = "Receive notifications from ShopSmart",
                        icon = Icons.Default.Notifications,
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED) {
                                    showPermissionDialog = true
                                    return@NotificationSwitchItemWithImageVector
                                }
                            }
                            notificationsEnabled = enabled
                            notificationPrefs.setNotificationsEnabled(enabled)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE0E0E0)
                    )

                    // Alert Settings Section
                    Text(
                        "Alert Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF006D3B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    NotificationSwitchItemWithImageVector(
                        title = "Sound",
                        subtitle = "Play sound for notifications",
                        icon = Icons.Default.Notifications,
                        checked = soundEnabled,
                        enabled = notificationsEnabled,
                        onCheckedChange = {
                            soundEnabled = it
                            notificationPrefs.setSoundEnabled(it)
                        }
                    )

                    NotificationSwitchItemWithDrawable(
                        title = "Vibration",
                        subtitle = "Vibrate for notifications",
                        iconResId = R.drawable.vibration_24px,
                        checked = vibrationEnabled,
                        enabled = notificationsEnabled,
                        onCheckedChange = {
                            vibrationEnabled = it
                            notificationPrefs.setVibrationEnabled(it)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE0E0E0)
                    )

                    // Notification Types Section
                    Text(
                        "Notification Types",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF006D3B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    NotificationSwitchItemWithDrawable(
                        title = "Promotional",
                        subtitle = "Receive offers and promotional updates",
                        iconResId = R.drawable.app_promo_24px,
                        checked = promotionalEnabled,
                        enabled = notificationsEnabled,
                        onCheckedChange = {
                            promotionalEnabled = it
                            notificationPrefs.setPromotionalEnabled(it)
                        }
                    )

                    NotificationSwitchItemWithImageVector(
                        title = "Order Updates",
                        subtitle = "Get updates about your orders",
                        icon = Icons.Default.ShoppingCart,
                        checked = orderUpdatesEnabled,
                        enabled = notificationsEnabled,
                        onCheckedChange = {
                            orderUpdatesEnabled = it
                            notificationPrefs.setOrderUpdatesEnabled(it)
                        }
                    )

                    NotificationSwitchItemWithDrawable(
                        title = "Delivery Updates",
                        subtitle = "Track your delivery status",
                        iconResId = R.drawable.package_2_24px,
                        checked = deliveryUpdatesEnabled,
                        enabled = notificationsEnabled,
                        onCheckedChange = {
                            deliveryUpdatesEnabled = it
                            notificationPrefs.setDeliveryUpdatesEnabled(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Permission Dialog
    if (showPermissionDialog) {
        NotificationPermissionDialog(
            onDismiss = { showPermissionDialog = false },
            onGoToSettings = {
                showPermissionDialog = false
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
    }
}

@Composable
private fun NotificationSwitchItemWithImageVector(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    }
}

@Composable
private fun NotificationSwitchItemWithDrawable(
    title: String,
    subtitle: String,
    iconResId: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
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
                    tint = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    }
}