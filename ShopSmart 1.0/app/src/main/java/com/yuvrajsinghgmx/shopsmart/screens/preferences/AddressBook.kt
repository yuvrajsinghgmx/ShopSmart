package com.yuvrajsinghgmx.shopsmart.screens.preferences

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class AddressEntry(val name: String, val address: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBookScreen(navController: NavController, sharedPreferences: SharedPreferences) {
    val viewModel = remember { AddressViewModel(sharedPreferences) } // Pass SharedPreferences to ViewModel
    val backgroundColor = Color(0xFFF6F5F3)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = backgroundColor,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = "Address Book",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var name by remember { mutableStateOf(TextFieldValue("")) }
                var address by remember { mutableStateOf(TextFieldValue("")) }

                // Input Fields
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { /* Handle next action */ })
                )

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
                )

                Button(
                    onClick = {
                        if (name.text.isNotBlank() && address.text.isNotBlank()) {
                            viewModel.addAddress(name.text, address.text)
                            name = TextFieldValue("")
                            address = TextFieldValue("")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Address")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Address List
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (viewModel.addresses.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp) // Space for bottom navigation
                        ) {
                            items(viewModel.addresses) { entry ->
                                AddressItem(entry) {
                                    viewModel.removeAddress(entry)
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No addresses added yet",
                                color = Color(0xFF637478)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddressItem(entry: AddressEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = entry.name, fontWeight = FontWeight.Bold)
                Text(text = entry.address)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Address"
                )
            }
        }
    }
}

// ViewModel to manage the addresses
class AddressViewModel(private val sharedPreferences: SharedPreferences) {
    var addresses by mutableStateOf(loadAddresses())
        private set

    fun addAddress(name: String, address: String) {
        addresses = addresses + AddressEntry(name, address)
        saveAddresses(addresses) // Persist addresses
    }

    fun removeAddress(entry: AddressEntry) {
        addresses = addresses.filter { it != entry }
        saveAddresses(addresses) // Persist addresses
    }

    private fun saveAddresses(addresses: List<AddressEntry>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(addresses)
        editor.putString("addresses", json)
        editor.apply()
    }

    private fun loadAddresses(): List<AddressEntry> {
        val json = sharedPreferences.getString("addresses", null) ?: return emptyList()
        val type = object : TypeToken<List<AddressEntry>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }
}
