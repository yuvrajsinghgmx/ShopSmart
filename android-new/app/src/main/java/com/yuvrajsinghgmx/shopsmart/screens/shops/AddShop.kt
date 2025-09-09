package com.yuvrajsinghgmx.shopsmart.screens.shops

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yuvrajsinghgmx.shopsmart.sharedComponents.ButtonLoader
import com.yuvrajsinghgmx.shopsmart.sharedComponents.FullScreenMapPickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    navController: NavController,
    onShopAdded: () -> Unit
) {
    val shopResponse by viewModel.shopResponse.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val stateValue = viewModel.state.value

    LaunchedEffect(shopResponse) {
        if (shopResponse != null) onShopAdded()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppTopBar("Add Shop",onBack = { navController.popBackStack() })
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                ShopPhotoPicker(
                    imageUri = stateValue.imageUri,
                    onImagePicked = { uri -> viewModel.onImagePicked(uri) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShopTextField(
                    label = "Shop Name",
                    value = stateValue.name,
                    onValueChange = { viewModel.onNameChanged(it) },
                    placeholder = "Enter your Shop name"
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShopDropdown(
                    label = "Shop Category",
                    options = listOf("Select category", "grocery", "clothing", "electronics"),
                    selected = stateValue.category,
                    onSelected = { viewModel.onCategorySelected(it) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShopTextField(
                    label = "Phone Number",
                    value = stateValue.phoneNumber,
                    onValueChange = { viewModel.onPhoneChanged(it) },
                    placeholder = "Enter 10-digit number",
                    keyboardType = KeyboardType.Phone,
                    isError = stateValue.isPhoneError,
                    errorMessage = "Phone number must be exactly 10 digits"
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShopLocationPicker(
                    selectedLocation = stateValue.location,
                    isPicking = stateValue.isPickingLocation,
                    onPickLocation = { location, address ->
                        viewModel.onLocationPicked(location, address)
                    },
                    onStartPicking = { viewModel.startLocationPicking() }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShopDescriptionField(
                    value = stateValue.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    maxChars = 1000,
                    isError = stateValue.isDescriptionError
                )
                Spacer(modifier = Modifier.height(40.dp))
                SaveShopButton(
                    isEnabled = viewModel.isFormValid(),
                    isLoading = isLoading,
                    onClick = { viewModel.saveShop(context) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

    LaunchedEffect(error) {
        error?.let { snackbarHostState.showSnackbar(it) }
    }
}

@Composable
fun AppTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ShopPhotoPicker(imageUri: Uri?, onImagePicked: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Shop Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Add Shop Photo", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun ShopTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
    if (isError && errorMessage != null) {
        Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onBackground) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
        }
    }
}

@Composable
fun ShopLocationPicker(
    selectedLocation: LatLng?,
    isPicking: Boolean,
    onPickLocation: (LatLng, String) -> Unit,
    onStartPicking: () -> Unit
) {
    Text("Shop Address", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation ?: LatLng(0.0, 0.0), 12f)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isPicking) 400.dp else 120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        selectedLocation?.let {
            val markerState = remember(it) { MarkerState(position = it) }
            LaunchedEffect(it) { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 12f)) }
            GoogleMap(cameraPositionState = cameraPositionState) { Marker(markerState) }
        } ?: Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = onStartPicking,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text("Pick Location on Map")
    }
    if (isPicking) {
        FullScreenMapPickerDialog(
            initialLocation = selectedLocation,
            onLocationConfirmed = onPickLocation,
            onDismiss = {}
        )
    }
}

@Composable
fun ShopDescriptionField(value: String, onValueChange: (String) -> Unit, maxChars: Int, isError: Boolean) {
    Text("Shop Description", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= maxChars) onValueChange(it) },
        modifier = Modifier.fillMaxWidth().height(100.dp),
        isError = isError,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            errorPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        placeholder = { Text("Tell customers about your shop") },
        supportingText = {
            Text(
                "${value.length}/$maxChars",
                style = MaterialTheme.typography.bodySmall,
                color = if (value.length > maxChars) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }
    )
}

@Composable
fun SaveShopButton(isEnabled: Boolean, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        if (isLoading) {
            ButtonLoader()
        } else {
            Text("Save Shop", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}