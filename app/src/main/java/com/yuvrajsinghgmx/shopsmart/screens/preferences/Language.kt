package com.yuvrajsinghgmx.shopsmart.screens.preferences

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*

// Data class to represent a language option
data class LanguageOption(
    val code: String,
    val name: String,
    val localName: String? = null
)

// Language preferences manager using the new AppCompat approach
class LanguageManager(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "language_prefs"
        private const val SELECTED_LANGUAGE = "selected_language"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLanguage(languageCode: String) {
        prefs.edit().putString(SELECTED_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(SELECTED_LANGUAGE, Locale.getDefault().language) ?: "en"
    }

    fun applyLanguage(activity: Activity) {
        val locale = Locale(getLanguage())
        val config = activity.resources.configuration
        config.setLocale(locale)
        activity.createConfigurationContext(config)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val languageManager = remember { LanguageManager(context) }
    var selectedLanguage by remember { mutableStateOf(languageManager.getLanguage()) }
    var showRestartDialog by remember { mutableStateOf(false) }

    val languages = remember {
        listOf(
            LanguageOption("en", "English"),
            LanguageOption("es", "Spanish", "Español"),
            LanguageOption("fr", "French", "Français"),
            LanguageOption("de", "German", "Deutsch"),
            LanguageOption("it", "Italian", "Italiano"),
            LanguageOption("hi", "Hindi", "हिंदी"),
            LanguageOption("zh", "Chinese", "中文"),
            LanguageOption("ja", "Japanese", "日本語"),
            LanguageOption("ko", "Korean", "한국어"),
            LanguageOption("ar", "Arabic", "العربية")
        )
    }

    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text("Restart Required") },
            text = { Text("The app needs to restart to apply the language change. Would you like to restart now?") },
            confirmButton = {
                TextButton(onClick = {
                    // Restart the app
                    val intent = activity.packageManager
                        .getLaunchIntentForPackage(activity.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                    activity.finish()
                }) {
                    Text("Restart Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestartDialog = false }) {
                    Text("Later")
                }
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
                        text = "Language",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Language List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                items(languages) { language ->
                    LanguageItem(
                        language = language,
                        isSelected = selectedLanguage == language.code,
                        onSelect = {
                            if (selectedLanguage != language.code) {
                                selectedLanguage = language.code
                                languageManager.setLanguage(language.code)
                                showRestartDialog = true
                            }
                        }
                    )
                }

                // Add bottom spacing for navigation bar
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Restart Dialog
        if (showRestartDialog) {
            AlertDialog(
                onDismissRequest = { showRestartDialog = false },
                title = { Text("Restart Required") },
                text = { Text("The app needs to restart to apply the language change. Would you like to restart now?") },
                confirmButton = {
                    TextButton(onClick = {
                        // Restart the app
                        val intent = activity.packageManager
                            .getLaunchIntentForPackage(activity.packageName)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.finish()
                    }) {
                        Text("Restart Now")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRestartDialog = false }) {
                        Text("Later")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageItem(
    language: LanguageOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
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
            Column {
                Text(
                    text = language.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                language.localName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
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