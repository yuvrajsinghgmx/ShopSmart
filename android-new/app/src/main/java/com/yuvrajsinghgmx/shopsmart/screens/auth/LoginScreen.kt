package com.yuvrajsinghgmx.shopsmart.screens.auth

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.auth.state.AuthState
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel
import com.yuvrajsinghgmx.shopsmart.sharedComponents.ButtonLoader
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedAppViewModel,
    onLogInSuccess: (Boolean) -> Unit,
    authPrefs: AuthPrefs
) {
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var showOtpField by remember { mutableStateOf(false) }

    val initialTime = 120
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
            is AuthState.AuthSuccess -> {
                onLogInSuccess(authPrefs.isNewUser())
            }
            is AuthState.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
            }
            is AuthState.Idle -> {
                showOtpField = false
                isTimerRunning = false
                ticks = initialTime
            }
            else -> Unit
        }
    }

    LaunchedEffect(isTimerRunning) {
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
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.shield_image), "Shield Logo")
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                if (!showOtpField) "Welcome to ShopSmart" else "Enter OTP",
                style = ShopSmartTypography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (!showOtpField) "Enter your mobile number to continue"
                else "We've sent a code to your number",
                style = ShopSmartTypography.bodyLarge,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

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
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading && (!showOtpField || otp.isNotEmpty())
            ) {
                if (isLoading) {
                    ButtonLoader()
                } else {
                    Text(
                        if (!showOtpField) "Send OTP" else "Verify OTP",
                        fontSize = 16.sp,
                        style = ShopSmartTypography.headlineLarge
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            if (showOtpField) {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            viewModel.resetState()
                            ticks = initialTime
                            isTimerRunning = false
                        },
                        enabled = !isLoading
                    ) {
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
                        Text(
                            if (isTimerRunning) "Resend OTP in ${
                                String.format(Locale.US,"%02d:%02d", minutes, seconds)
                            }" else "Resend OTP"
                        )
                    }
                }
                if (isLoading) {
                    Text(
                        "Verifying automatically…",
                        style = ShopSmartTypography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                Text(
                    "By continuing, you agree to our Terms & Privacy Policy",
                    style = ShopSmartTypography.labelSmall,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    lineHeight = 1.5.em
                )
            }
        }
    }
}

@Composable
fun PhoneInputSection(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    enabled: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        ) {
            Text("+91", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Icon(
                Icons.Default.Phone,
                "Phone",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            VerticalDivider(
                modifier = Modifier.height(28.dp),
                color = MaterialTheme.colorScheme.outline
            )
            TextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = { Text("Phone number") },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
fun OtpInputSection(otp: String, onOtpChange: (String) -> Unit, enabled: Boolean) {
    OutlinedTextField(
        value = otp,
        onValueChange = onOtpChange,
        label = { Text("6-Digit OTP") },
        enabled = enabled,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
    )
}