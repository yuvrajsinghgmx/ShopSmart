package com.yuvrajsinghgmx.shopsmart.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yuvrajsinghgmx.shopsmart.sharedComponents.FullScreenMapPickerDialog
import com.yuvrajsinghgmx.shopsmart.sharedprefs.UserDataStore
import com.yuvrajsinghgmx.shopsmart.ui.theme.BackgroundDark
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary
import com.yuvrajsinghgmx.shopsmart.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopScreen(navController: NavController) {
    var shopName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var shopDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Select category") }
    var expanded by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isPickingLocation by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

    val context = LocalContext.current
    val userDataStore = remember { UserDataStore(context) }

    val user by userDataStore.userFlow.collectAsState(initial = null)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(user) {
        user?.let {
            Log.d("UserDataStore", "Full Name: ${it.fullName}")
            Log.d("UserDataStore", "Email: ${it.email ?: "N/A"}")
            Log.d("UserDataStore", "Role: ${it.role}")
            Log.d("UserDataStore", "Profile URI: ${it.profileImageUri ?: "N/A"}")
        } ?: Log.d("UserDataStore", "No user found")
    }

    val categories = listOf(
        "Select category",
        "Restaurant",
        "Grocery Store",
        "Electronics",
        "Clothing",
        "Pharmacy",
        "Others"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Add Shop",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = BackgroundDark,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack()  }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                // Empty IconButton to balance the navigation icon
                IconButton(
                    onClick = { },
                    enabled = false
                ) {
                    // Invisible spacer
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )


        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Shop Photo Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = Color(0xFFE5E5E5),
                            shape = CircleShape
                        )
                        .background(Color(0xFFF8F8F8))
                        .clickable { launcher.launch("image/*")  }
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Add Photo",
                            tint = NavySecondary, //Color(0xFF4CAF50),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Add Shop Photo",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Shop Name Field
            Text(
                text = "Shop Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = shopName,
                onValueChange = { shopName = it },
                placeholder = {
                    Text(
                        text = "Enter your Shop name",
                        color = Color(0xFFBBBBBB)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFE5E5E5)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Shop Category Field
            Text(
                text = "Shop Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown arrow",
                            tint = Purple40
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color(0xFFE5E5E5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(text = category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Phone Number Field
            Text(
                text = "Phone Number",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    // Allow only digits and limit length to 10
                    val filtered = input.filter { it.isDigit() }.take(10)

                    phoneNumber = filtered
                    isError = filtered.length != 10 && filtered.isNotEmpty()
                },
                placeholder = {
                    Text(
                        text = "Enter 10-digit number",
                        color = Color(0xFFBBBBBB)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Color.Red else Color(0xFF4CAF50),
                    unfocusedBorderColor = if (isError) Color.Red else Color(0xFFE5E5E5)
                ),
                isError = isError,
                shape = RoundedCornerShape(8.dp)
            )

            if (isError) {
                Text(
                    text = "Phone number must be exactly 10 digits",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Shop Address Section
            Text(
                text = "Shop Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isPickingLocation) 400.dp else 120.dp) // expand height when picking
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE5E5E5),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                selectedLocation?.let { location ->
                    val markerState = remember(location) { MarkerState(position = location) }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(location, 12f)
                        },
                        uiSettings = MapUiSettings(
                            scrollGesturesEnabled = false,
                            zoomControlsEnabled = false,
                            tiltGesturesEnabled = false
                        )
                    ) {
                        Marker(state = markerState, title = "Selected Location")
                    }
                } ?: Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "No location",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pick Location Button
            Button(
                onClick = { isPickingLocation = true
                          },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavySecondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Pick Location on Map",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (isPickingLocation) {
                FullScreenMapPickerDialog(
                    initialLocation = selectedLocation,
                    onLocationConfirmed = { location ->
                        selectedLocation = location
                        isPickingLocation = false
                    },
                    onDismiss = { isPickingLocation = false }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))



            // Shop Description Field
            Text(
                text = "Shop Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = shopDescription,
                onValueChange = { input ->
                    val words = input.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
                    if (words.size <= 1000) {
                        shopDescription = input
                        isError = false
                    } else {
                        isError = true
                    }
                },
                placeholder = {
                    Text(
                        text = "Tell customers about your shop...",
                        color = Color(0xFFBBBBBB)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Color.Red else Color(0xFF4CAF50),
                    unfocusedBorderColor = if (isError) Color.Red else Color(0xFFE5E5E5)
                ),
                shape = RoundedCornerShape(8.dp),
                maxLines = 4,
                isError = isError
            )

            if (isError) {
                Text(
                    text = "Maximum limit of 1000 words reached",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Save Shop Button
            Button(
                onClick = { navController.navigate("main_graph"){
                    popUpTo("login_route") { inclusive = true }
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Save Shop",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun AddShopScreenPreview() {
    AddShopScreen()
}*/
