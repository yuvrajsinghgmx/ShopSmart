package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlin.collections.isNotEmpty

@Composable
fun FullScreenMapPickerDialog(
    initialLocation: LatLng?,
    onLocationConfirmed: (LatLng, String) -> Unit,
    onDismiss: () -> Unit
) {
    var tempLocation by remember { mutableStateOf(initialLocation) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            tempLocation ?: LatLng(37.7749, -122.4194),
            12f
        )
    }

    // For search
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    val context = LocalContext.current
    val placesClient = remember {
        Places.createClient(context)
    }

    fun searchPlaces(input: String) {
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(input)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                Log.d("PlacesAPI", "Predictions: ${response.autocompletePredictions}")
                predictions = response.autocompletePredictions
            }
            .addOnFailureListener {  e ->
                Log.e("PlacesAPI", "Error: ", e)
                predictions = emptyList()
            }
    }

    fun getAddressFromLatLng(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) addresses[0].getAddressLine(0) ?: ""
            else ""
        } catch (e: Exception) {
            ""
        }
    }

    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Google Map
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

            // Search Bar
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
                        .background(Color.White)
                )

                DropdownMenu(
                    expanded = predictions.isNotEmpty(),
                    onDismissRequest = { predictions = emptyList() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    predictions.forEach { prediction ->
                        DropdownMenuItem(
                            text = { Text(prediction.getFullText(null).toString()) },
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
                                            cameraPositionState.position =
                                                CameraPosition.fromLatLngZoom(latLng, 15f)
                                        }
                                    }
                            }
                        )
                    }
                }
            }

            // Confirm button at the bottom
            Button(
                onClick = { tempLocation?.let { latLng ->
                    val address = getAddressFromLatLng(latLng)
                    onLocationConfirmed(latLng, address) // pass address back
                }},
                enabled = tempLocation != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavySecondary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            // Close button at top right
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
    }
}

