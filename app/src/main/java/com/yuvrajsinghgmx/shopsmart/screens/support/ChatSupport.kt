package com.yuvrajsinghgmx.shopsmart.screens.support


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSupportScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8F8F8),
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
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = "Chat Support",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "How can we help you?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ChatOptions()
            }

            // Chat Input Field at Bottom
            ChatInputField()
        }
    }
}

@Composable
fun ChatOptions() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Option for FAQs
        ChatOptionItem(
            icon = R.drawable.contact_support_24px,
            title = "FAQs",
            description = "Find answers to frequently asked questions."
        )

        // Option for Contact Support
        ChatOptionItem(
            icon = R.drawable.contact_phone_24px,
            title = "Contact Support",
            description = "Connect with a support agent for assistance."
        )

        // Option for Feedback
        ChatOptionItem(
            icon = R.drawable.library_books_24px,
            title = "Feedback",
            description = "Send us your feedback to improve our service."
        )
    }
}

@Composable
fun ChatOptionItem(icon: Int, title: String, description: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF1A73E8)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun ChatInputField() {
    var message by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFF8F8F8),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (message.isEmpty()) {
                        Text(text = "Type your message...", color = Color.Gray)
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { /* Handle send message */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Message",
                    tint = Color(0xFF1A73E8)
                )
            }
        }
    }
}
