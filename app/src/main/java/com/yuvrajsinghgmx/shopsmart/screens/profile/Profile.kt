package com.yuvrajsinghgmx.shopsmart.screens.profile

import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.components.ImagePreviewDialog
import com.yuvrajsinghgmx.shopsmart.utils.ImageHelper
import com.yuvrajsinghgmx.shopsmart.utils.SharedPrefsHelper
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(modifier: Modifier = Modifier,navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lightBackgroundColor = Color(0xFFF6F5F3)
    var isEditing by remember { mutableStateOf(false) }



    // Initialize state variables
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePreview by remember { mutableStateOf(false) }
    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Fetch data once when composable is first composed
    LaunchedEffect(Unit) {
        userName = SharedPrefsHelper.getUserName(context)
        userEmail = SharedPrefsHelper.getUserEmail(context)
        profilePhotoUri = SharedPrefsHelper.getProfilePhotoUri(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                val copiedUri = ImageHelper.copyImageToPrivateStorage(context, it)
                copiedUri?.let { savedUri ->
                    profilePhotoUri = savedUri
                    SharedPrefsHelper.saveProfilePhotoUri(context, savedUri)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor)
    ){
        CenterAlignedTopAppBar(
            modifier = Modifier.padding(0.dp),
            title = {
                Text(
                    "Profile",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF332D25)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = lightBackgroundColor
            )
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(150.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profilePhotoUri ?: R.drawable.profilenewone)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable {
                            if (isEditing) {
                                launcher.launch("image/*")
                            } else {
                                showImagePreview = true
                            }
                        },
                    contentScale = ContentScale.Crop,
                    fallback = painterResource(id = R.drawable.profilenewone),
                    error = painterResource(id = R.drawable.profilenewone)
                )
                if (isEditing) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-8).dp)
                            .zIndex(1f)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .padding(8.dp)
                            .clickable { launcher.launch("image/*") },
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isEditing) {
                TextField(
                    value = userName,
                    onValueChange = {
                        userName = it
                        // Re-validate the name field as the user types
                        isNameError = it.isEmpty()
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isNameError,  // If there's an error, highlight the text field
                    supportingText = {
                        if (isNameError) {
                            Text("Name cannot be empty", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        Color(0xFF233b41),
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userEmail,
                    onValueChange = {
                        userEmail = it
                        // Re-validate the email field as the user types
                        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmailError,  // If there's an error, highlight the text field
                    supportingText = {
                        if (isEmailError) {
                            Text("Invalid email address format", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            } else {
                Text(
                    userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF233b41)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (isEditing) {
                        isNameError = userName.isEmpty()
                        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()

                        if (!isNameError && !isEmailError) {
                            SharedPrefsHelper.saveUserName(context, userName)
                            SharedPrefsHelper.saveUserEmail(context, userEmail)
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                            isEditing = false
                            // No need to refreshProfileData here
                        } else {
                            Toast.makeText(context, "Please correct the errors", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        isEditing = true
                    }
                },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.13f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF637478)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(if (isEditing) "Save Profile" else "Edit Profile", color = Color(0xFF637478), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Text(
//                        "Settings",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF233b41)
//                    )
//                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .align(Alignment.Start),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(modifier = Modifier.padding(1.dp)) {
                        ProfileItem(title = "Notifications Settings", Icon = R.drawable.bell) {
                            navController.navigate("notifications")  // Updated navigation
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider()
                        ProfileItem(title = "My Orders", Icon = R.drawable.checkout) {
                            navController.navigate("coming_soon")
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(4.dp))
                        ProfileItem(title = "Settings", Icon = R.drawable.setting) {
                            navController.navigate("settings")  // New navigation to Settings screen
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(4.dp))
                        ProfileItem(title = "Help & Support", Icon = R.drawable.help) {
                            navController.navigate("Help")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Clear the user data and navigate to the login screen
                        SharedPrefsHelper.clearUserData(context)
                        navController.navigate("signUpScreen") {
                            popUpTo("Profile") { inclusive = true } // Clear back stack
                        }

                    },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFebeded)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Log Out", color = Color(0xFF637478), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showImagePreview) {
        ImagePreviewDialog(
            imageUri = profilePhotoUri,
            defaultImageRes = R.drawable.profile
        ) {
            showImagePreview = false
        }
    }
}

@Composable
fun ProfileItem(title: String, Icon: Int, onClick: () -> Unit = {}) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = Icon),
                contentDescription = "Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFF637478))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF637478),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
    }
}