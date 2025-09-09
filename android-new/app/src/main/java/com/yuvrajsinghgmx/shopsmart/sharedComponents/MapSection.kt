package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun FullScreenMapPickerDialog(
    initialLocation: LatLng?,
    onLocationConfirmed: (LatLng, String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var tempLocation by remember { mutableStateOf<LatLng?>(initialLocation) }
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val placesClient = remember { Places.createClient(context) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            tempLocation ?: LatLng(28.6139, 77.2088),
            12f
        )
    }

    LaunchedEffect(Unit) {
        if (tempLocation == null) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    tempLocation = userLatLng
                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(userLatLng, 15f),
                            durationMs = 1000
                        )
                    }
                } ?: run {
                    tempLocation = LatLng(37.7749, -122.4194)
                }
            }.addOnFailureListener {
                tempLocation = LatLng(37.7749, -122.4194)
            }
        }
    }

    fun searchPlaces(input: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(input)
            .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response -> predictions = response.autocompletePredictions }
            .addOnFailureListener { e ->
                Log.e("PlacesAPI", "Error: ", e)
                predictions = emptyList()
            }
    }

    fun getAddressFromLatLng(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) addresses[0].getAddressLine(0) ?: "" else ""
        } catch (e: Exception) {
            "${e.printStackTrace()}"
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng -> tempLocation = latLng }
            ) {
                tempLocation?.let { location ->
                    val markerState = remember(location) { MarkerState(position = location) }
                    Marker(state = markerState)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        if (it.length > 2) searchPlaces(it)
                    },
                    placeholder = { Text("Search location...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                )

                DropdownMenu(
                    expanded = predictions.isNotEmpty(),
                    onDismissRequest = { predictions = emptyList() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    predictions.forEach { prediction ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    prediction.getFullText(null).toString(),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            onClick = {
                                predictions = emptyList()
                                query = prediction.getFullText(null).toString()
                                val placeId = prediction.placeId
                                val placeFields = listOf(Place.Field.LAT_LNG)
                                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                                placesClient.fetchPlace(request)
                                    .addOnSuccessListener { response ->
                                        response.place.latLng?.let { latLng ->
                                            tempLocation = latLng
                                            scope.launch {
                                                cameraPositionState.animate(
                                                    update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                                                    durationMs = 1000
                                                )
                                            }
                                        }
                                    }
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    tempLocation?.let { latLng ->
                        val address = getAddressFromLatLng(latLng)
                        onLocationConfirmed(latLng, address)
                    }
                },
                enabled = tempLocation != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm", fontWeight = FontWeight.Medium)
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
