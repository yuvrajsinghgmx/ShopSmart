package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    val isPhoneValid = phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
    val isOtpValid = otp.length == 6 && otp.all { it.isDigit() }
    val isFormValid = isPhoneValid && isOtpValid

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.shield_image),
                    contentDescription = "Shield Logo"
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Welcome to ShopSmart",
                style = ShopSmartTypography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Enter your mobile number and OTP to continue",
                style = ShopSmartTypography.bodyLarge,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(24.dp))

            // Phone number input
            Card(
                modifier = modifier
                    .fillMaxWidth(0.95f)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+91",
                        fontSize = 16.sp,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.Blue,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))

                    TextField(
                        value = phoneNumber,
                        onValueChange = { if (it.length <= 10) phoneNumber = it },
                        placeholder = {
                            Text(
                                text = "Phone number",
                                color = Color(0xFFBBBBBB)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF333333),
                            unfocusedTextColor = Color(0xFF333333)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                if (!isPhoneValid && phoneNumber.isNotEmpty()) {
                    Text(
                        text = "Phone number must be 10 digits",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            // OTP input
            Card(
                modifier = modifier
                    .fillMaxWidth(0.95f)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "OTP",
                        tint = Color.Blue,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))

                    TextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        placeholder = {
                            Text(
                                text = "Enter OTP",
                                color = Color(0xFFBBBBBB)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF333333),
                            unfocusedTextColor = Color(0xFF333333)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                if (!isOtpValid && otp.isNotEmpty()) {
                    Text(
                        text = "OTP must be 6 digits",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            // Submit button
            Button(
                onClick = { /* Handle login */ },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF4CAF50) else Color.Gray,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    style = ShopSmartTypography.headlineLarge
                )
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy",
                style = ShopSmartTypography.labelSmall,
                fontSize = 14.sp,
                color = Color(0xFF999999),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 1.5.em
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
