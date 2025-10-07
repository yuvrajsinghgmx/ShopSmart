package com.yuvrajsinghgmx.shopsmart.screens.shops

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.yuvrajsinghgmx.shopsmart.sharedComponents.rememberCameraLauncher

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
            AppTopBar("Add Shop", onBack = { navController.popBackStack() })
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    ShopTextField(
                        label = "Shop Name",
                        value = stateValue.name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        placeholder = "Enter your Shop name"
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    ShopDropdown(
                        label = "Shop Category",
                        options = listOf("grocery", "clothing", "electronics"),
                        selected = stateValue.category,
                        onSelected = { viewModel.onCategorySelected(it) }
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    ShopLocationPicker(
                        selectedLocation = stateValue.location,
                        isPicking = stateValue.isPickingLocation,
                        onPickLocation = { location, address ->
                            viewModel.onLocationPicked(location, address)
                        },
                        onStartPicking = { viewModel.startLocationPicking() },
                        viewModel = viewModel
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    ShopDescriptionField(
                        value = stateValue.description,
                        onValueChange = { viewModel.onDescriptionChanged(it) },
                        maxChars = 1000,
                        isError = stateValue.isDescriptionError
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    FilePicker(
                        title = "Shop Images",
                        uris = stateValue.imageUris,
                        onUrisPicked = { uris -> viewModel.onImagesPicked(uris) },
                        onRemoveUri = { uri -> viewModel.removeImage(uri) },
                        maxCount = 3,
                        allowedMimeTypes = "image/*",
                        showAsRow = true
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    FilePicker(
                        title = "Shop Documents",
                        uris = stateValue.documentUris,
                        onUrisPicked = { uris -> viewModel.onDocumentsPicked(uris) },
                        onRemoveUri = { uri -> viewModel.removeDocument(uri) },
                        maxCount = 5,
                        allowedMimeTypes = "image/*", // Now only allows images for documents
                        showAsRow = false
                    )
                }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item {
                    SaveShopButton(
                        isEnabled = viewModel.isFormValid(),
                        isLoading = isLoading,
                        onClick = { viewModel.saveShop(context) }
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
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
fun ShopTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(bottom = 8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
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
            placeholder = { Text("Select category") },
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
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
    onStartPicking: () -> Unit,
    viewModel : ShopViewModel
) {
    var address by remember { mutableStateOf<String?>(null) }
    Text("Shop Address", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation ?: LatLng(10.0000, 79.7500), 12f)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isPicking) 400.dp else 120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        selectedLocation?.let {
            val markerState = remember(it) { MarkerState(position = it) }
            LaunchedEffect(it) { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 12f)) }
            GoogleMap(cameraPositionState = cameraPositionState) { Marker(markerState) }
        } ?: Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(modifier = Modifier.height(4.dp))
    address?.let {
        Text(it, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 4.dp))
    }
    Spacer(modifier = Modifier.height(6.dp))
    Button(
        onClick = onStartPicking,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
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
            selAddress = { address = it },
            onDismiss = {viewModel.cancelLocationPicking()}
        )
    }
}

@Composable
fun ShopDescriptionField(value: String, onValueChange: (String) -> Unit, maxChars: Int, isError: Boolean) {
    Text("Shop Description", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= maxChars) onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        isError = isError,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
        ),
        placeholder = { Text("Tell customers about your shop") },
        supportingText = {
            Text(
                text = "${value.length}/$maxChars",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                color = if (value.length > maxChars) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
fun FilePicker(
    title: String,
    uris: List<Uri>,
    onUrisPicked: (List<Uri>) -> Unit,
    onRemoveUri: (Uri) -> Unit,
    maxCount: Int,
    allowedMimeTypes: String,
    showAsRow: Boolean
) {
    var showSourceDialog by remember { mutableStateOf(false) }

    val launchCamera = rememberCameraLauncher { uri ->
        onUrisPicked(listOf(uri))
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { resultUris ->
            if (uris.size + resultUris.size <= maxCount) {
                onUrisPicked(resultUris)
            }
        }
    )
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleMedium)
            Text(
                "${uris.size}/$maxCount",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (showAsRow) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uris) { uri ->
                    FilePreview(uri = uri, onRemove = onRemoveUri)
                }
                if (uris.size < maxCount) {
                    item { AddFileButton { showSourceDialog = true } }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uris.forEach { uri ->
                    FilePreview(uri = uri, onRemove = onRemoveUri, modifier = Modifier.fillMaxWidth())
                }
                if (uris.size < maxCount) {
                    AddFileButton(modifier = Modifier.fillMaxWidth()) { showSourceDialog = true }
                }
            }
        }
    }


    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Choose Source") },
            text = { Text("Select a source for your image.") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    galleryLauncher.launch(allowedMimeTypes)
                }) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    launchCamera()
                }) {
                    Text("Camera")
                }
            }
        )
    }
}
@Composable
fun AddFileButton(modifier: Modifier = Modifier, onAdd: () -> Unit) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.medium
            )
            .clickable(onClick = onAdd),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.AddAPhoto, contentDescription = "Add File", tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun FilePreview(uri: Uri, onRemove: (Uri) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(100.dp)) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { onRemove(uri) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Remove File", tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun SaveShopButton(isEnabled: Boolean, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (isLoading) {
            ButtonLoader()
        } else {
            Text("Save Shop", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}