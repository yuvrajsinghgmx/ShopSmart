package com.yuvrajsinghgmx.shopsmart.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapSection(
    selectedLocation: LatLng?,
    onLocationSelected: (LatLng) -> Unit
) {
    var cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(
            selectedLocation ?: LatLng(37.7749, -122.4194), // Default San Francisco
            12f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // bigger height for map
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE5E5E5),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                onLocationSelected(latLng) // Capture clicked location
            }
        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected Location"
                )
            }
        }
    }
}

@Composable
fun FullScreenMapPickerDialog(
    initialLocation: LatLng?,
    onLocationConfirmed: (LatLng) -> Unit,
    onDismiss: () -> Unit
) {
    var tempLocation by remember { mutableStateOf(initialLocation) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        tempLocation ?: LatLng(37.7749, -122.4194),
                        12f
                    )
                },
                onMapClick = { latLng -> tempLocation = latLng }
            ) {
                tempLocation?.let { location ->
                    val markerState = remember(location) { MarkerState(position = location) }
                    Marker(state = markerState)
                }
            }

            // Confirm button at the bottom
            Button(
                onClick = { tempLocation?.let { onLocationConfirmed(it) } },
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

