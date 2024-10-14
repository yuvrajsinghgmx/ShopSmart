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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@Composable
fun Upcoming(modifier: Modifier = Modifier) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }

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

        Button(
            onClick = {}
        ) {
            Text("Add Item")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUpcoming(){
    ShopSmartTheme {
        Upcoming()
    }
}
