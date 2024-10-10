package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun Upcoming(
    modifier: Modifier = Modifier, 
    onAddItem: (String, String, String) -> Unit // New callback for adding items
) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemDate by remember { mutableStateOf("") } // New state for date

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Item")

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = itemPrice,
            onValueChange = { itemPrice = it },
            label = { Text("Item Price") },
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = itemDate,
            onValueChange = { itemDate = it },
            label = { Text("Due Date (yyyy-MM-dd)") }, // Date input as string
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                onAddItem(itemName, itemPrice, itemDate) // Pass data back to ViewModel or parent
            }
        ) {
            Text("Add Item")
        }
    }
}
