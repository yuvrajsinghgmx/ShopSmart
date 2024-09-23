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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeScreen() {
    var items by remember { mutableStateOf(SnapshotStateList<String>()) }
    var newItem by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Box (modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){

                Text(
                    text = "ShopSmart",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium ,
                    modifier = Modifier.padding(20.dp),
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { item ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Text(
                            text = item,
                            modifier = Modifier
                                .weight(1f)
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
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Button(
                    onClick = {
                        if (newItem.isNotBlank()) {
                            items.add(newItem)
                            newItem = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add Item")
                }
            }
        }
    }
}
