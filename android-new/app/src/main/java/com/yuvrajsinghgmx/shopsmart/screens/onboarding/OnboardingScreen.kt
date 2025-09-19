package com.yuvrajsinghgmx.shopsmart.screens.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationServices
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel
import com.yuvrajsinghgmx.shopsmart.sharedComponents.ButtonLoader
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary
import com.yuvrajsinghgmx.shopsmart.utils.uriToFile
import java.util.Locale

@Composable
fun OnBoardingScreen(
    onboardingViewmodel: SharedAppViewModel,
    onboardingComplete: (UserRole) -> Unit
) {
    val authState by onboardingViewmodel.authState.collectAsState()
    val context = LocalContext.current
    val isOnboarding by onboardingViewmodel.isOnboarding.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val rememberPermissionState = rememberLocationPermissionState()

    // Error states
    var nameError by remember { mutableStateOf(false) }
    var roleError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (val state=authState) {
            is AuthState.onboardingSuccess -> {
                onboardingComplete(state.role)
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Complete Your Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )

        //// Profile Photo Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
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
                    tint = NavySecondary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = "Add Photo",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
        )

        // Full Name Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Full Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    if (it.length <= 16 && !it.contains("\n")) fullName = it
                },
                placeholder = {
                    Text(
                        text = "Enter your full name",
                        color = Color.LightGray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            if (nameError) {
                Text("Name is required", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Role Selection
        Text(
            text = "Select Your Role",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleCard(
                role = UserRole.CUSTOMER,
                isSelected = selectedRole == UserRole.CUSTOMER,
                onClick = { selectedRole = UserRole.CUSTOMER },
                modifier = Modifier.weight(1f)
            )

            RoleCard(
                role = UserRole.SHOP_OWNER,
                isSelected = selectedRole == UserRole.SHOP_OWNER,
                onClick = { selectedRole = UserRole.SHOP_OWNER },
                modifier = Modifier.weight(1f)
            )
        }

        if (roleError) {
            Text("Please select a role", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Email (Optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "Enter your email",
                        color = Color.LightGray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailError,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            if (emailError) {
                Text("Invalid email format", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Continue Button
        Button(
            onClick = {

                nameError = fullName.isBlank()
                roleError = selectedRole == null
                emailError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()

                try {
                    if (!nameError && !roleError && !emailError && selectedRole != null) {
                        if (rememberPermissionState) {
                            getUserLocation(context) { lat, long, address ->
                                onboardingViewmodel.completeOnboarding(
                                    role = selectedRole!!.name,
                                    fullName = fullName,
                                    address = address,
                                    latitude = lat,
                                    longitude = long,
                                    radius = 10,
                                    imageFile = imageUri?.let { uriToFile(context, it) },
                                    email = email // convert Uri -> File
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("OnBoardingScreen", "Error: ${e.message}")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !isOnboarding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if(isOnboarding){
                ButtonLoader()
                return@Button
            }else {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun RoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray

    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = role.icon,
                contentDescription = role.title,
                tint = NavySecondary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = role.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class UserRole(val title: String, val icon: ImageVector) {
    CUSTOMER("Customer", Icons.Default.ShoppingBag),
    SHOP_OWNER("Shop Owner", Icons.Default.Store)
}

@SuppressLint("MissingPermission")
fun getUserLocation(context: Context, onResult: (Double, Double, String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude

            // Reverse geocode into human-readable address
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressText: Any = try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0) ?: ""
            } catch (e: Exception) {
                Log.e("LocationError", "Error getting address: ${e.message}")
            }

            onResult(latitude, longitude, addressText as String)
        }
    }
}

