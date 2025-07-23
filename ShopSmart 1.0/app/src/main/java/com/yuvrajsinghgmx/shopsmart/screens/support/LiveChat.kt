package com.yuvrajsinghgmx.shopsmart.screens.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveChatScreen(navController: NavController) {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isAgentTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = Color(0xFFF6F5F3)

    // Initial greeting message
    LaunchedEffect(Unit) {
        messages = listOf(
            ChatMessage(
                "Hello! Welcome to ShopSmart support. How can I help you today?",
                false,
                LocalDateTime.now()
            )
        )
    }

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

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Live Chat",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF332D25)
                        )
                        Text(
                            "Support Agent",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF637478)
                        )
                    }
                }
            }

            // Chat Messages
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message)
                    }

                    if (isAgentTyping) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                TypingIndicator()
                            }
                        }
                    }
                }
            }

            // Chat Input
            ChatInput(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = {
                    if (inputText.isNotBlank()) {
                        messages = messages + ChatMessage(inputText, true)
                        inputText = ""
                        isAgentTyping = true

                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                            delay(2000)
                            val response = getAutomatedResponse(messages.last().content)
                            messages = messages + ChatMessage(response, false)
                            isAgentTyping = false
                            delay(100)
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) {
        Color(0xFF006D40)
    } else {
        Color.White
    }

    val textColor = if (message.isUser) {
        Color.White
    } else {
        Color(0xFF332D25)
    }

    // Using Arrangement instead of Alignment for Row
    val arrangement = if (message.isUser) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    val shape = if (message.isUser) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Surface(
            shape = shape,
            color = bubbleColor,
            tonalElevation = if (!message.isUser) 1.dp else 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.content,
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = message.timestamp.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    color = if (message.isUser) Color.White.copy(alpha = 0.7f)
                    else Color(0xFF637478),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Type your message...") },
                maxLines = 3,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onSend() }
                )
            )

            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (value.isNotBlank()) Color(0xFF006D40) else Color(0xFF637478)
                )
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    val transition = rememberInfiniteTransition()
    val dots = 3

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(dots) { index ->
            val alpha by transition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = index * 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF637478).copy(alpha = alpha))
            )
        }
    }
}

private fun getAutomatedResponse(userMessage: String): String {
    return when {
        userMessage.contains("refund", ignoreCase = true) ->
            "I understand you have a question about refunds. Could you please provide your order number so I can help you better?"

        userMessage.contains("order", ignoreCase = true) ->
            "I'll be happy to help you with your order. Can you share more details about your concern?"

        userMessage.contains("delivery", ignoreCase = true) ->
            "For delivery related queries, I can assist you. What specific information do you need?"

        userMessage.contains("thank", ignoreCase = true) ->
            "You're welcome! Is there anything else I can help you with?"

        else -> "Thank you for your message. Could you please provide more details about your query so I can assist you better?"
    }
}