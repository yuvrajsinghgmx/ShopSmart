package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    // Animation values
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Logo rotation animation
    val rotationAnim = animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "rotation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000) // Show splash for 3 seconds
        onSplashComplete() // Call the completion callback
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFF8F9FA)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Logo Container with green diamond shape
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        Color(0xFF4CAF50),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                // Shopping bag icon (you can replace with actual icon)
                Text(
                    text = "🛒",
                    fontSize = 64.sp,
                    color = Color.White,
                    modifier = Modifier.scale(rotationAnim.value + 1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "ShopSmart",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Local shops, smart search.",
                fontSize = 16.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
        }

        // Animated loading indicator at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Loading dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val animatedScale = animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 600,
                                delayMillis = index * 200
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_scale_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .scale(animatedScale.value)
                            .background(
                                Color(0xFF4CAF50),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading text
            Text(
                text = "Loading...",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                fontWeight = FontWeight.Medium
            )
        }

        // Version text at bottom
        Text(
            text = "v1.0.0",
            fontSize = 12.sp,
            color = Color(0xFFBBBBBB),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(alphaAnim.value)
        )
    }
}