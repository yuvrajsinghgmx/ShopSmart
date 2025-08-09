package com.yuvrajsinghgmx.shopsmart.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.state.AuthState
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.viewmodeluser.AuthViewModel
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onExitClick: () -> Unit
) {
    // --- State Management ---
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var showOtpField by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.CodeSent -> {
                showOtpField = true
                scope.launch { snackbarHostState.showSnackbar("OTP Sent!") }
            }
            is AuthState.AuthSuccess -> {
                scope.launch { snackbarHostState.showSnackbar("Login Successful!") }
                onLoginSuccess()
            }
            is AuthState.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
            }
            is AuthState.Idle -> {
                showOtpField = false
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        // FROM: Color.White
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { /* No title needed */ },
                actions = {
                    IconButton(onClick = onExitClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit to Home",
                            // Tint will adapt automatically
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        // FROM: Color(0xFFF5F5F5)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.shield_image),
                        contentDescription = "Shield Logo"
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = if (!showOtpField) "Welcome to ShopSmart" else "Enter OTP",
                    style = ShopSmartTypography.headlineLarge
                    // Color is inherited from the theme
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (!showOtpField) "Enter your mobile number to continue" else "We've sent a code to your number",
                    style = ShopSmartTypography.bodyLarge,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))

                if (!showOtpField) {
                    PhoneInputSection(phoneNumber = phoneNumber, onPhoneNumberChange = { phoneNumber = it })
                } else {
                    OtpInputSection(otp = otp, onOtpChange = { otp = it })
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!showOtpField) {
                            if (activity != null) {
                                val fullPhoneNumber = "+91$phoneNumber"
                                viewModel.sendOtp(fullPhoneNumber, activity)
                            } else {
                                // Handle the unlikely case where activity is null
                                scope.launch {
                                    snackbarHostState.showSnackbar("Error: Could not perform action.")
                                }
                            }
                        } else {
                            viewModel.verifyOtp(otp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),

                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (!showOtpField) "Send OTP" else "Verify OTP",
                        fontSize = 16.sp,
                        style = ShopSmartTypography.headlineLarge
                    )
                }

                Spacer(Modifier.height(24.dp))
                if (!showOtpField) {
                    Text(
                        text = "By continuing, you agree to our Terms & Privacy Policy",
                        style = ShopSmartTypography.labelSmall,
                        fontSize = 14.sp,

                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp),
                        lineHeight = 1.5.em
                    )
                } else {
                    TextButton(onClick = { viewModel.resetState() }) {
                        Text("Use a different number")
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneInputSection(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp),
        // FROM: CardDefaults.cardColors(containerColor = Color.White)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        ) {
            // FROM: color = Color(0xFF333333)
            Text("+91", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            // FROM: tint = Color.Blue
            Icon(Icons.Default.Phone, "Phone", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp).size(20.dp))
            Spacer(Modifier.width(8.dp))
            // FROM: color = Color.Gray
            VerticalDivider(modifier = modifier.height(28.dp), color = MaterialTheme.colorScheme.outline)
            TextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = { Text("Phone number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                // REMOVED: Hardcoded transparent colors to use theme defaults
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun OtpInputSection(
    modifier: Modifier = Modifier,
    otp: String,
    onOtpChange: (String) -> Unit
) {
    OutlinedTextField(
        value = otp,
        onValueChange = onOtpChange,
        label = { Text("6-Digit OTP") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        // Let the default OutlinedTextField colors handle the theming
    )
}