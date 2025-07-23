package com.yuvrajsinghgmx.shopsmart.screens.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PasswordRequirement(
    val label: String,
    var isCompleted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChangePasswordScreen(navController: NavController) {
    //initializing
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val passwordRequirements = remember {
        mutableStateListOf(
            PasswordRequirement("At least 8 characters"),
            PasswordRequirement("At least one uppercase letter"),
            PasswordRequirement("At least one number"),
            PasswordRequirement("At least one special character"),
            PasswordRequirement("Passwords match")
        )
    }

    fun validatePassword(password: String) {
        passwordRequirements[0].isCompleted = password.length >= 8
        passwordRequirements[1].isCompleted = password.any { it.isUpperCase() }
        passwordRequirements[2].isCompleted = password.any { it.isDigit() }
        passwordRequirements[3].isCompleted = password.any { !it.isLetterOrDigit() }
        passwordRequirements[4].isCompleted = newPassword == confirmPassword && newPassword.isNotEmpty()
    }

    LaunchedEffect(newPassword, confirmPassword) {
        validatePassword(newPassword)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TopBar without extra padding
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Change Password",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1D1B20)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFFBFBFB)
            ),
            modifier = Modifier.padding(0.dp),
            windowInsets = WindowInsets(0.dp)
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Password Field
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showCurrentPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                trailingIcon = {
                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                        Icon(
                            painter = painterResource(
                                if (showCurrentPassword)
                                    R.drawable.visibility_off_24px
                                else
                                    R.drawable.visibility_24px
                            ),
                            contentDescription = if (showCurrentPassword)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            )

            // New Password Field
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    validatePassword(it)
                },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showNewPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            painter = painterResource(
                                if (showNewPassword)
                                    R.drawable.visibility_off_24px
                                else
                                    R.drawable.visibility_24px
                            ),
                            contentDescription = if (showNewPassword)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            )

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    validatePassword(newPassword)
                },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showConfirmPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            painter = painterResource(
                                if (showConfirmPassword)
                                    R.drawable.visibility_off_24px
                                else
                                    R.drawable.visibility_24px
                            ),
                            contentDescription = if (showConfirmPassword)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            )

            // Password Requirements List
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFF0F7FF)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Password Requirements",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    passwordRequirements.forEach { requirement ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (requirement.isCompleted)
                                        R.drawable.check_circle_24px
                                    else
                                        R.drawable.radio_button_unchecked_24px
                                ),
                                contentDescription = null,
                                tint = if (requirement.isCompleted)
                                    Color(0xFF006D40)
                                else
                                    Color(0xFF637478),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                requirement.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (requirement.isCompleted)
                                    Color(0xFF006D40)
                                else
                                    Color(0xFF637478)
                            )
                        }
                    }
                }
            }

            // Change Password Button
            Button(
                onClick = {
                    keyboardController?.hide()
                    scope.launch {
                        if (passwordRequirements.all { it.isCompleted }) {
                            isLoading = true
                            delay(1500) // Simulate API call
                            if (currentPassword == "oldpass") { // Demo validation
                                showSuccessDialog = true
                            } else {
                                errorMessage = "Current password is incorrect"
                                showErrorDialog = true
                            }
                            isLoading = false
                        } else {
                            errorMessage = "Please meet all password requirements"
                            showErrorDialog = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Change Password")
                }
            }

            // Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFFFF4ED)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info_24px),
                        contentDescription = null,
                        tint = Color(0xFFB25E02)
                    )
                    Text(
                        "For security reasons, you'll be logged out after changing your password.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB25E02)
                    )
                }
            }
        }
    }

    // Dialogs
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Success", fontWeight = FontWeight.Bold) },
            text = { Text("Your password has been changed successfully. Please log in again.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error", fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }
}