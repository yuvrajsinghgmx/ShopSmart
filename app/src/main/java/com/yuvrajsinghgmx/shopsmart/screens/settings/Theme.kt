package com.yuvrajsinghgmx.shopsmart.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ThemeOption(
    val name: String,
    val theme: Theme,
    val icon: ImageVector
)

enum class Theme {
    LIGHT, DARK, SYSTEM
}

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val SELECTED_THEME = "selected_theme"

    private val _currentTheme = MutableStateFlow(Theme.SYSTEM)
    val currentTheme: StateFlow<Theme> = _currentTheme

    fun setTheme(context: Context, theme: Theme) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(SELECTED_THEME, theme.name)
            .apply()
        _currentTheme.value = theme
    }

    fun getTheme(context: Context): Theme {
        val themeName = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(SELECTED_THEME, Theme.SYSTEM.name)
        return Theme.valueOf(themeName ?: Theme.SYSTEM.name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedTheme by remember { mutableStateOf(ThemeManager.getTheme(context)) }

    val themes = remember {
        listOf(
            ThemeOption(
                name = "Light",
                theme = Theme.LIGHT,
                icon = Icons.Filled.FavoriteBorder  // Using Light Border Favourites for Light Mode
            ),
            ThemeOption(
                name = "Dark",
                theme = Theme.DARK,
                icon = Icons.Filled.Favorite // Using Filled Favourites for Dark Mode
            ),
            ThemeOption(
                name = "System Default",
                theme = Theme.SYSTEM,
                icon = Icons.Filled.Settings  // Using Settings for System
            )
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Theme",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
            items(themes) { themeOption ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = {
                        selectedTheme = themeOption.theme
                        ThemeManager.setTheme(context, themeOption.theme)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedTheme == themeOption.theme)
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
                                imageVector = themeOption.icon,
                                contentDescription = null,
                                tint = if (selectedTheme == themeOption.theme)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = themeOption.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (selectedTheme == themeOption.theme) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}