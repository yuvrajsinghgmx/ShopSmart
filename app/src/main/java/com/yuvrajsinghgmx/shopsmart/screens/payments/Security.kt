package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG
}

sealed class PasswordState {
    data class Success(val message: String) : PasswordState()
    data class Error(val message: String) : PasswordState()
    object Loading : PasswordState()
}

class PasswordViewModel : ViewModel() {
    private val _passwordStrength = MutableStateFlow<PasswordStrength>(PasswordStrength.WEAK)
    val passwordStrength: StateFlow<PasswordStrength> = _passwordStrength

    private val _passwordState = MutableStateFlow<PasswordState?>(null)
    val passwordState: StateFlow<PasswordState?> = _passwordState

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun validatePassword(password: String) {
        var score = 0

        if (password.length >= 8) score++
        if (password.contains(Regex("[A-Z]"))) score++
        if (password.contains(Regex("[a-z]"))) score++
        if (password.contains(Regex("[0-9]"))) score++
        if (password.contains(Regex("[^A-Za-z0-9]"))) score++

        _passwordStrength.value = when (score) {
            0, 1 -> PasswordStrength.WEAK
            2, 3 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        when {
            currentPassword.isEmpty() -> {
                _snackbarMessage.value = "Current password is required"
            }
            newPassword.isEmpty() -> {
                _snackbarMessage.value = "New password is required"
            }
            newPassword != confirmPassword -> {
                _snackbarMessage.value = "New passwords don't match"
            }
            _passwordStrength.value == PasswordStrength.WEAK -> {
                _snackbarMessage.value = "Password is too weak"
            }
            else -> {
                _passwordState.value = PasswordState.Loading
                // In real implementation, make API call here
                _passwordState.value = PasswordState.Success("Password Changed Successfully")
            }
        }
    }

    fun clearPasswordState() {
        _passwordState.value = null
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SecurityScreen(navController: NavController) {
    val passwordViewModel: PasswordViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lightBackgroundColor = Color(0xFFF6F5F3)

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val passwordStrength by passwordViewModel.passwordStrength.collectAsState()
    val passwordState by passwordViewModel.passwordState.collectAsState()
    val snackbarMessage by passwordViewModel.snackbarMessage.collectAsState()

    LaunchedEffect(passwordState) {
        when (passwordState) {
            is PasswordState.Success -> {
                snackbarHostState.showSnackbar(
                    message = (passwordState as PasswordState.Success).message,
                    duration = SnackbarDuration.Short
                )
                delay(100)
                passwordViewModel.clearPasswordState()
                navController.navigateUp()
            }
            is PasswordState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (passwordState as PasswordState.Error).message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            passwordViewModel.clearSnackbarMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5F3))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF6F5F3),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF332D25)
                        )
                    }

                    Text(
                        text = "Security",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332D25),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Password Change Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Change Password",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        // Current Password Field
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showCurrentPassword)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = if (showCurrentPassword)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = if (showCurrentPassword)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Gray
                                    )
                                }
                            }
                        )

                        // New Password Field
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = {
                                newPassword = it
                                passwordViewModel.validatePassword(it)
                            },
                            label = { Text("New Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showNewPassword)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = if (showNewPassword)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = if (showNewPassword)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Gray
                                    )
                                }
                            }
                        )

                        // Password Strength Indicator
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = when (passwordStrength) {
                                    PasswordStrength.WEAK -> 0.33f
                                    PasswordStrength.MEDIUM -> 0.66f
                                    PasswordStrength.STRONG -> 1f
                                },
                                modifier = Modifier.fillMaxWidth(),
                                color = when (passwordStrength) {
                                    PasswordStrength.WEAK -> Color.Red
                                    PasswordStrength.MEDIUM -> Color(0xFFFFB74D)
                                    PasswordStrength.STRONG -> Color(0xFF4CAF50)
                                }
                            )

                            Text(
                                text = "Password Strength: ${passwordStrength.name}",
                                color = when (passwordStrength) {
                                    PasswordStrength.WEAK -> Color.Red
                                    PasswordStrength.MEDIUM -> Color(0xFFFF9800)
                                    PasswordStrength.STRONG -> Color(0xFF4CAF50)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Confirm Password Field
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm New Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showConfirmPassword)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = if (showConfirmPassword)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = if (showConfirmPassword)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Gray
                                    )
                                }
                            },
                            isError = newPassword != confirmPassword && confirmPassword.isNotEmpty()
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    passwordViewModel.changePassword(
                                        currentPassword,
                                        newPassword,
                                        confirmPassword
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = currentPassword.isNotEmpty() &&
                                    newPassword.isNotEmpty() &&
                                    confirmPassword.isNotEmpty() &&
                                    passwordStrength != PasswordStrength.WEAK &&
                                    newPassword == confirmPassword
                        ) {
                            Text("Update Password")
                        }
                    }
                }

                // Password Requirements Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Password Requirements",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text("• Minimum 8 characters")
                        Text("• At least one uppercase letter")
                        Text("• At least one lowercase letter")
                        Text("• At least one number")
                        Text("• At least one special character")
                    }
                }

                // Security Tips Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Security Tips",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text("• Never share your password with anyone")
                        Text("• Use different passwords for different accounts")
                        Text("• Avoid using personal information in your password")
                        Text("• Change your password regularly")
                        Text("• Enable two-factor authentication when available")
                    }
                }
            }
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}