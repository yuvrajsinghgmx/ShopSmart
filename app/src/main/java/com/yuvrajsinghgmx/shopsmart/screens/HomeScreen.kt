package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    var items by remember { mutableStateOf(mutableListOf<String>()) }
    var newItem by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "ShopSmart", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier) {
            items(items) { item ->
                var isChecked by remember { mutableStateOf(false) }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(start = 8.dp),
                        color = if (isChecked) Color.Gray else Color.Black,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                    )
                    IconButton(onClick = {
                        items.remove(item)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newItem,
                onValueChange = { newItem = it },
                placeholder = { Text("Add new item") },
                modifier = Modifier.padding(end = 8.dp)
            )
            Button(onClick = {
                if (newItem.isNotBlank()) {
                    items.add(newItem)
                    newItem = ""
                }
            }) {
                Text("+")
            }
        }
    }
}
