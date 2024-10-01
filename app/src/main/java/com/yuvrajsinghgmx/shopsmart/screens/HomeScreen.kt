package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.yuvrajsinghgmx.shopsmart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var items by remember { mutableStateOf(SnapshotStateList<String>()) }
    var newItem by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF6200EA), Color(0xFF03DAC6))
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ShopSmart",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BackgroundImage()
            Column(
                modifier = Modifier
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
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(10.dp)
                                )

                                .padding(12.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EA))
                            )
                            Text(
                                text = item,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontSize = 18.sp,
                                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                            )
                            IconButton(onClick = {
                                items.remove(item)
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItem,
                        onValueChange = { newItem = it },
                        label = { Text("Add new item") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .shadow(6.dp, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6200EA),
                            unfocusedBorderColor = Color(0xFF6200EA),
                            focusedLabelColor = Color(0xFFF8F4F4),
                            unfocusedLabelColor = Color(0xFFD2E1E0),
                            cursorColor = Color(0xFF83600B)
                        ),
                        textStyle = TextStyle(color = Color.White)
                    )


                    FloatingActionButton(
                        onClick = {
                            if (newItem.isNotBlank()) {
                                items.add(newItem)
                                newItem = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        shape = RoundedCornerShape(50),
                        containerColor = Color(0xFF6200EA)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Item",
                            tint = Color.White
                        )
                    }
                }

            }
        }

    }
}

@Composable
fun BackgroundImage() {
    val alphaValue = if (isSystemInDarkTheme()) 0.8f else 0.99f

    Image(
        painter = painterResource(id = R.drawable.shopping_cart),
        contentDescription = "Background Image",
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alphaValue),
        contentScale = ContentScale.FillHeight
    )
}

