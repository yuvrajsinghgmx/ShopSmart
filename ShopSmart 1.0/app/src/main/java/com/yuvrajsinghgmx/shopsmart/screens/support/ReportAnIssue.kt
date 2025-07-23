package com.yuvrajsinghgmx.shopsmart.screens.support

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReportAnIssueScreen(
    onBackPressed: () -> Unit,
    onSubmitIssue: (String, String) -> Unit
) {
    val context = LocalContext.current
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Main Column Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))  // Light background color
            .padding(16.dp)
    ) {
        // Back Button Row with Arrow Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBackPressed() }  // Handle back press
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Report an Issue",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields
        BasicTextField(
            value = subject,
            onValueChange = { subject = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .height(50.dp),
            decorationBox = { innerTextField ->
                if (subject.isEmpty()) {
                    Text(text = "Enter subject", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .height(100.dp),
            decorationBox = { innerTextField ->
                if (description.isEmpty()) {
                    Text(text = "Enter description", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button with Loader
        Button(
            onClick = {
                isLoading = true
                onSubmitIssue(subject, description)

                coroutineScope.launch {
                    delay(2000)  // Simulate a delay
                    isLoading = false
                    Toast.makeText(context, "Feedback Sent", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Submit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cancel Button
        OutlinedButton(
            onClick = { onBackPressed() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Cancel", fontSize = 16.sp)
        }
    }
}