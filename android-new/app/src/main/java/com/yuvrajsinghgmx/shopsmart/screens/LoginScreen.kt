package com.yuvrajsinghgmx.shopsmart.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onExitClick: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var showOtpField by remember { mutableStateOf(false) }

    val initialTime = 120 // 2 minutes
    var ticks by remember { mutableIntStateOf(initialTime) }
    var isTimerRunning by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val isLoading = authState is AuthState.Loading

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.CodeSent -> {
                showOtpField = true
                isTimerRunning = true
                scope.launch { snackbarHostState.showSnackbar("OTP Sent!") }
            }
            is AuthState.AuthSuccess -> onLoginSuccess()
            is AuthState.Error -> scope.launch { snackbarHostState.showSnackbar(state.message) }
            is AuthState.Idle -> {
                showOtpField = false
                isTimerRunning = false
                ticks = initialTime
            }
            else -> Unit
        }
    }

    LaunchedEffect(key1 = isTimerRunning) {
        if (isTimerRunning) {
            while (ticks > 0) {
                delay(1000)
                ticks--
            }
            isTimerRunning = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onExitClick) {
                        Icon(Icons.Default.Close, "Exit to Home")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.shield_image), "Shield Logo")
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(if (!showOtpField) "Welcome to ShopSmart" else "Enter OTP", style = ShopSmartTypography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Text(if (!showOtpField) "Enter your mobile number to continue" else "We've sent a code to your number", style = ShopSmartTypography.bodyLarge, fontSize = 20.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            if (!showOtpField) {
                PhoneInputSection(phoneNumber, { phoneNumber = it }, !isLoading)
            } else {
                OtpInputSection(otp, { otp = it }, !isLoading)
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!showOtpField) {
                        if (activity != null) {
                            viewModel.sendInitialOtp("+91$phoneNumber", activity)
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Error: Could not perform action.") }
                        }
                    } else {
                        viewModel.verifyOtp(otp)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text(if (!showOtpField) "Send OTP" else "Verify OTP", fontSize = 16.sp, style = ShopSmartTypography.headlineLarge)
                }
            }
            Spacer(Modifier.height(24.dp))

            if (showOtpField) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    TextButton(onClick = { viewModel.resetState() }, enabled = !isLoading) {
                        Text("Change Number")
                    }
                    TextButton(
                        enabled = !isTimerRunning && !isLoading,
                        onClick = {
                            if (activity != null) {
                                ticks = initialTime
                                isTimerRunning = true
                                viewModel.resendOtp("+91$phoneNumber", activity)
                            }
                        }
                    ) {
                        val minutes = ticks / 60
                        val seconds = ticks % 60
                        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

                        Text(
                            if (isTimerRunning) "Resend OTP in $formattedTime" else "Resend OTP"
                        )
                    }

                }
            } else {
                Text("By continuing, you agree to our Terms & Privacy Policy", style = ShopSmartTypography.labelSmall, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp), lineHeight = 1.5.em)
            }
        }
    }
}

@Composable
fun PhoneInputSection(phoneNumber: String, onPhoneNumberChange: (String) -> Unit, enabled: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(0.95f).padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        ) {
            Text("+91", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.Phone, "Phone", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp).size(20.dp))
            Spacer(Modifier.width(8.dp))
            VerticalDivider(modifier = Modifier.height(28.dp), color = MaterialTheme.colorScheme.outline)
            TextField(
                value = phoneNumber,
                onValueChange = { newText ->
                    if (newText.length <= 10) {
                        onPhoneNumberChange(newText)
                    }
                },
                placeholder = { Text("Phone number") },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun OtpInputSection(otp: String, onOtpChange: (String) -> Unit, enabled: Boolean) {
    OutlinedTextField(
        value = otp,
        onValueChange = { otplength ->
            if (otplength.length <= 6) {
                onOtpChange(otplength)
            }
        },
        label = { Text("6-Digit OTP") },
        enabled = enabled,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
    )
}