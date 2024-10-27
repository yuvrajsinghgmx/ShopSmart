package com.yuvrajsinghgmx.shopsmart.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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

class PrivacyPreferences(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "privacy_prefs"
        private const val KEY_DATA_COLLECTION = "data_collection"
        private const val KEY_PERSONALIZED_ADS = "personalized_ads"
        private const val KEY_LOCATION_TRACKING = "location_tracking"
        private const val KEY_USAGE_STATS = "usage_stats"
        private const val KEY_CRASH_REPORTS = "crash_reports"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setDataCollection(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DATA_COLLECTION, enabled).apply()
    }

    fun getDataCollection(): Boolean = prefs.getBoolean(KEY_DATA_COLLECTION, true)

    fun setPersonalizedAds(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PERSONALIZED_ADS, enabled).apply()
    }

    fun getPersonalizedAds(): Boolean = prefs.getBoolean(KEY_PERSONALIZED_ADS, true)

    fun setLocationTracking(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LOCATION_TRACKING, enabled).apply()
    }

    fun getLocationTracking(): Boolean = prefs.getBoolean(KEY_LOCATION_TRACKING, true)

    fun setUsageStats(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_USAGE_STATS, enabled).apply()
    }

    fun getUsageStats(): Boolean = prefs.getBoolean(KEY_USAGE_STATS, true)

    fun setCrashReports(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CRASH_REPORTS, enabled).apply()
    }

    fun getCrashReports(): Boolean = prefs.getBoolean(KEY_CRASH_REPORTS, true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val privacyPrefs = remember { PrivacyPreferences(context) }

    var dataCollectionEnabled by remember {
        mutableStateOf(privacyPrefs.getDataCollection())
    }
    var personalizedAdsEnabled by remember {
        mutableStateOf(privacyPrefs.getPersonalizedAds())
    }
    var locationTrackingEnabled by remember {
        mutableStateOf(privacyPrefs.getLocationTracking())
    }
    var usageStatsEnabled by remember {
        mutableStateOf(privacyPrefs.getUsageStats())
    }
    var crashReportsEnabled by remember {
        mutableStateOf(privacyPrefs.getCrashReports())
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Privacy Settings",
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
                // Data Collection Section
                SectionTitle("Data Collection")

                PrivacyListItem(
                    title = "Data Collection",
                    subtitle = "Allow app to collect usage data for better service",
                    iconResId = R.drawable.analytics_24px,
                    checked = dataCollectionEnabled,
                    onCheckedChange = {
                        dataCollectionEnabled = it
                        privacyPrefs.setDataCollection(it)
                    }
                )

                PrivacyListItem(
                    title = "Personalized Ads",
                    subtitle = "Allow personalized ads based on your preferences",
                    iconResId = R.drawable.ad_24px,
                    checked = personalizedAdsEnabled,
                    onCheckedChange = {
                        personalizedAdsEnabled = it
                        privacyPrefs.setPersonalizedAds(it)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                // Location & Tracking Section
                SectionTitle("Location & Tracking")

                PrivacyListItem(
                    title = "Location Tracking",
                    subtitle = "Allow app to access your location",
                    iconResId = R.drawable.distance_24px,
                    checked = locationTrackingEnabled,
                    onCheckedChange = {
                        locationTrackingEnabled = it
                        privacyPrefs.setLocationTracking(it)
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                // App Analytics Section
                SectionTitle("App Analytics")

                PrivacyListItem(
                    title = "Usage Statistics",
                    subtitle = "Share app usage statistics for improvements",
                    iconResId = R.drawable.query_stats_24px,
                    checked = usageStatsEnabled,
                    onCheckedChange = {
                        usageStatsEnabled = it
                        privacyPrefs.setUsageStats(it)
                    }
                )

                PrivacyListItem(
                    title = "Crash Reports",
                    subtitle = "Send crash reports to improve app stability",
                    iconResId = R.drawable.bug_report_24px,
                    checked = crashReportsEnabled,
                    onCheckedChange = {
                        crashReportsEnabled = it
                        privacyPrefs.setCrashReports(it)
                    }
                )

                // Privacy Links Section
                SectionTitle("Privacy Information")

                PrivacyListLinkItem(
                    title = "Privacy Policy",
                    subtitle = "Read our privacy policy",
                    iconResId = R.drawable.verified_user_24px,
                    onClick = { navController.navigate("privacy_policy") }
                )

                PrivacyListLinkItem(
                    title = "Terms of Service",
                    subtitle = "View terms of service",
                    iconResId = R.drawable.gavel_24px,
                    onClick = { navController.navigate("terms") }
                )

                // Bottom Spacing
                Spacer(modifier = Modifier.height(16.dp))
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
private fun PrivacyListItem(
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
                        color = MaterialTheme.colorScheme.onSurface,
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

@Composable
private fun PrivacyListLinkItem(
    title: String,
    subtitle: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick,
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
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.assistant_direction_24px),
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}