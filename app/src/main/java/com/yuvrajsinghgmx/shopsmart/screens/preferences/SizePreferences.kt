package com.yuvrajsinghgmx.shopsmart.screens.preferences

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

data class SizeCategory(
    val name: String,
    val icon: Int,
    val sizes: List<String>,
    var selectedSize: String? = null
)

data class MeasurementPreference(
    val category: String,
    val measurement: String,
    val unit: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SizePreferencesScreen(navController: NavController) {
    val lightBackgroundColor = Color(0xFFF6F5F3)
    var showSaveConfirmation by remember { mutableStateOf(false) }

    // Size Categories
    var sizeCategories by remember {
        mutableStateOf(
            listOf(
                SizeCategory(
                    "Clothing",
                    R.drawable.clothing_24px,
                    listOf("XS", "S", "M", "L", "XL", "XXL"),
                    "M"
                ),
                SizeCategory(
                    "Shoes",
                    R.drawable.shoes_24px,
                    listOf("UK 6", "UK 7", "UK 8", "UK 9", "UK 10", "UK 11"),
                    "UK 8"
                ),
                SizeCategory(
                    "Waist",
                    R.drawable.waist_24px,
                    listOf("28", "30", "32", "34", "36", "38"),
                    "32"
                )
            )
        )
    }

    // Measurement Preferences
    var measurements by remember {
        mutableStateOf(
            listOf(
                MeasurementPreference("Height", "175", "cm"),
                MeasurementPreference("Weight", "70", "kg"),
                MeasurementPreference("Chest", "100", "cm"),
                MeasurementPreference("Shoulder", "45", "cm")
            )
        )
    }

    var measurementUnit by remember { mutableStateOf("Metric") } // or Imperial

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Size Preferences",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your Size Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Set your size preferences for better product recommendations",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Unit Preference
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Measurement Unit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilterChip(
                            selected = measurementUnit == "Metric",
                            onClick = { measurementUnit = "Metric" },
                            label = { Text("Metric (cm/kg)") }
                        )
                        FilterChip(
                            selected = measurementUnit == "Imperial",
                            onClick = { measurementUnit = "Imperial" },
                            label = { Text("Imperial (in/lb)") }
                        )
                    }
                }
            }

            // Size Categories
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Standard Sizes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    sizeCategories.forEach { category ->
                        CategorySizeSelector(
                            category = category,
                            onSizeSelected = { newSize ->
                                sizeCategories = sizeCategories.map {
                                    if (it.name == category.name) it.copy(selectedSize = newSize)
                                    else it
                                }
                            }
                        )
                        if (category != sizeCategories.last()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                    }
                }
            }

            // Measurements
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Detailed Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    measurements.forEach { measurement ->
                        MeasurementInput(
                            measurement = measurement,
                            isMetric = measurementUnit == "Metric"
                        )
                        if (measurement != measurements.last()) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            // Save Button
            Button(
                onClick = { showSaveConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E8545)
                )
            ) {
                Text("Save Preferences")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showSaveConfirmation) {
            AlertDialog(
                onDismissRequest = { showSaveConfirmation = false },
                title = { Text("Success") },
                text = { Text("Your size preferences have been saved.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSaveConfirmation = false
                            navController.navigateUp()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun CategorySizeSelector(
    category: SizeCategory,
    onSizeSelected: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = category.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            category.sizes.forEach { size ->
                FilterChip(
                    selected = size == category.selectedSize,
                    onClick = { onSizeSelected(size) },
                    label = { Text(size) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeasurementInput(
    measurement: MeasurementPreference,
    isMetric: Boolean
) {
    var value by remember { mutableStateOf(measurement.measurement) }
    val unit = if (isMetric) measurement.unit else {
        when (measurement.unit) {
            "cm" -> "in"
            "kg" -> "lb"
            else -> measurement.unit
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = measurement.category,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.width(100.dp),
            suffix = { Text(unit) },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
    }
}