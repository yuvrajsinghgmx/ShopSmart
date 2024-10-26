package com.yuvrajsinghgmx.shopsmart.screens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.profilefeatures.UserDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationScreen(
    navController: NavController,
    context: Context,
    sharedPreferences: SharedPreferences
) {
    val userPreferences = UserDataStore(context)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State for user data
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") }

    // Load saved data when screen is created
    LaunchedEffect(Unit) {
        name = sharedPreferences.getString("user_name", "") ?: ""
        email = sharedPreferences.getString("user_email", "") ?: ""
        phoneNumber = sharedPreferences.getString("user_phone", "") ?: ""
        countryCode = sharedPreferences.getString("user_country_code", "+91") ?: "+91"
    }

    // Save data when leaving screen
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                // Save data when app goes to background
                sharedPreferences.edit().apply {
                    putString("user_name", name)
                    putString("user_email", email)
                    putString("user_phone", phoneNumber)
                    putString("user_country_code", countryCode)
                    apply()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Personal Information",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Save data before navigating back
                        scope.launch {
                            sharedPreferences.edit().apply {
                                putString("user_name", name)
                                putString("user_email", email)
                                putString("user_phone", phoneNumber)
                                putString("user_country_code", countryCode)
                                apply()
                            }
                            navController.navigateUp()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF6F5F3)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF6F5F3)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                isError = name.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountryCodeDropdown(
                    selectedCode = countryCode,
                    onCodeSelected = { countryCode = it }
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= 10) phoneNumber = it
                    },
                    label = { Text("Phone Number") },
                    isError = phoneNumber.isNotEmpty() && phoneNumber.length != 10,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (validateInputs(name, email, phoneNumber)) {
                            // Save to both SharedPreferences and UserDataStore
                            sharedPreferences.edit().apply {
                                putString("user_name", name)
                                putString("user_email", email)
                                putString("user_phone", phoneNumber)
                                putString("user_country_code", countryCode)
                                apply()
                            }
                            userPreferences.saveUserData(name, email, "$countryCode$phoneNumber")
                            snackbarHostState.showSnackbar("Information Saved Successfully")

                            // Wait for a moment to show the success message
                            kotlinx.coroutines.delay(1000)
                            navController.navigateUp()
                        } else {
                            snackbarHostState.showSnackbar("Please fill all fields correctly")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

// Dropdown for selecting Country Code
@Composable
fun CountryCodeDropdown(selectedCode: String, onCodeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val countryCodes = listOf("+1", "+44", "+91", "+81") // Sample list

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedCode)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countryCodes.forEach { code ->
                DropdownMenuItem(
                    text = { Text(code) },
                    onClick = {
                        onCodeSelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Updated validation function
fun validateInputs(name: String, email: String, phoneNumber: String): Boolean {
    return name.isNotBlank() &&
            (email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
            (phoneNumber.isEmpty() || phoneNumber.length == 10)
}