package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.rememberLocationPermissionState
import kotlinx.coroutines.launch
import java.util.Locale

private fun getAddressFromLatLng(geocoder: Geocoder, latLng: LatLng, callback: (String) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
            callback(addresses.firstOrNull()?.toAddressLine() ?: "Unknown Location")
        }
    } else {
        @Suppress("DEPRECATION")
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            callback(addresses?.firstOrNull()?.toAddressLine() ?: "Unknown Location")
        } catch (e: Exception) {
            callback("Could not determine address")
        }
    }
}

private fun Address.toAddressLine(): String {
    return (0..maxAddressLineIndex).joinToString(separator = ", ") { getAddressLine(it) }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenMapPickerDialog(
    initialLocation: LatLng?,
    onLocationConfirmed: (LatLng, String) -> Unit,
    selAddress : (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val locationPermission = rememberLocationPermissionState()
    var selectedLocation by remember { mutableStateOf(initialLocation) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf(emptyList<com.google.android.libraries.places.api.model.AutocompletePrediction>()) }
    var isSearching by remember { mutableStateOf(true) }

    val placesClient = remember { Places.createClient(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val defaultLocation = LatLng(28.6139, 77.2088) // Delhi, India
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation ?: defaultLocation, 15f)
    }
    val markerState = remember { MarkerState(position = initialLocation ?: defaultLocation) }

        LaunchedEffect(locationPermission) {
            if (locationPermission) {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            if (userLocation == null) {
                                userLocation = LatLng(it.latitude, it.longitude)
                                selectedLocation = userLocation
                                markerState.position = userLocation!!
                                scope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(
                                            userLocation!!,
                                            15f
                                        )
                                    )
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Select Location") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close map")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        userLocation?.let {
                            scope.launch { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 16f)) }
                            selectedLocation = it
                            markerState.position = it
                            selectedLocation?.let { loc ->
                                getAddressFromLatLng(geocoder, loc) { address ->
                                    query = address
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 100.dp)
                ) {
                    Icon(Icons.Filled.MyLocation, "Current Location")
                }
            },
            bottomBar = {
                BottomAppBar(containerColor = MaterialTheme.colorScheme.surface) {
                    Button(
                        onClick = {
                            selectedLocation?.let { loc ->
                                getAddressFromLatLng(geocoder, loc) { address ->
                                    onLocationConfirmed(loc, address)
                                    selAddress(address)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        enabled = selectedLocation != null
                    ) {
                        Text("Confirm Location")
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                        markerState.position = latLng
                        selectedLocation?.let { loc ->
                            getAddressFromLatLng(geocoder, loc) { address ->
                               query = address
                            }
                        }
                        isSearching = true
                        predictions = emptyList()
                    }
                ) { Marker(state = markerState, draggable = true) }

                Column(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().padding(16.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), shadowElevation = 4.dp) {
                        Column {
                            OutlinedTextField(
                                value = query,
                                onValueChange = {
                                    query = it
                                    isSearching = true
                                    if (it.length > 2) {
                                        val token = AutocompleteSessionToken.newInstance()
                                        val request = FindAutocompletePredictionsRequest.builder().setSessionToken(token).setQuery(it).build()
                                        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                                            predictions = response.autocompletePredictions
                                        }
                                    } else predictions = emptyList()
                                },
                                placeholder = { Text("Search your shop location") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    if (query.isNotEmpty()) {
                                        IconButton(onClick = {
                                            query = ""
                                            predictions = emptyList()
                                        }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                                        }
                                    }
                                }
                            )

                            if (predictions.isNotEmpty() && isSearching) {
                                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 240.dp)) {
                                    items(predictions) { prediction ->
                                        Text(
                                            text = prediction.getFullText(null).toString(),
                                            modifier = Modifier.fillMaxWidth().clickable {
                                                query = prediction.getFullText(null).toString()
                                                predictions = emptyList()
                                                isSearching = false
                                                val placeFields = listOf(Place.Field.LAT_LNG)
                                                val request = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()
                                                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                                                    response.place.latLng ?.let { latLng ->
                                                        selectedLocation = latLng
                                                        markerState.position = latLng
                                                        scope.launch { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) }
                                                    }
                                                }
                                            }.padding(16.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}