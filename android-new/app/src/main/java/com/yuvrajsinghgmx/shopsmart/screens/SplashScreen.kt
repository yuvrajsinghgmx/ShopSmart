package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    // Animation values
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000) // Show splash for 3 seconds
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Diamond-shaped logo container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .rotate(45f) // Rotate to create diamond shape
                    .background(
                        Color(0xFF4CAF50),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Shopping bag icon from drawable - rotated back to normal
                Image(
                    painter = painterResource(id = R.drawable.ic_shopping_bag), // Your vector drawable
                    contentDescription = "Shopping Bag",
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(-45f) // Counter-rotate the icon
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // App Name
            Text(
                text = "ShopSmart",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Local shops, smart search.",
                fontSize = 16.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.Normal
            )
        }

        // Animated loading indicator at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Loading dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                            .size(8.dp)
                            .scale(animatedScale.value)
                            .background(
                                Color(0xFF4CAF50),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}