package com.yuvrajsinghgmx.shopsmart.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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

// Feedback Data Class to represent feedback items
data class FeedbackItem(
    val subject: String,
    val description: String
)

@Composable
fun FeedbackScreen(
    onBackPressed: () -> Unit,
    onSubmitFeedback: (String, String) -> Unit
) {
    val context = LocalContext.current
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Mock List of Feedback Items
    val feedbackList = remember {
        mutableStateListOf(
            FeedbackItem("App Crash", "The app crashes on startup."),
            FeedbackItem("UI Improvement", "The dark mode could be more vibrant."),
            FeedbackItem("Feature Request", "Please add a feedback tracking feature.")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Back Icon with Title
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
                    .clickable { onBackPressed() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Feedback Centre",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }

        // Feedback List Display using LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)  // Allow the list to take up available space
        ) {
            items(feedbackList) { feedback ->
                FeedbackItemCard(feedback)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields for New Feedback
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

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Feedback Button
        Button(
            onClick = {
                isLoading = true
                onSubmitFeedback(subject, description)

                coroutineScope.launch {
                    delay(2000)  // Simulate submission delay
                    isLoading = false
                    feedbackList.add(FeedbackItem(subject, description))
                    Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_SHORT).show()
                    subject = ""  // Reset input fields
                    description = ""
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
    }
}

@Composable
fun FeedbackItemCard(feedback: FeedbackItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = feedback.subject,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = feedback.description,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}