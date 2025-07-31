package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import com.yuvrajsinghgmx.shopsmart.ui.theme.Montserrat
import com.yuvrajsinghgmx.shopsmart.ui.theme.Roboto
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var phoneNumber by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White //can be changed for setting up of dark mode
    ) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
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
                    painter  = painterResource(R.drawable.shield_image),
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
                "Enter your mobile number to continue",
                style = ShopSmartTypography.bodyLarge,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(24.dp))

            //phone input row
            Card(
                modifier = modifier.fillMaxWidth(0.95f).padding(8.dp),
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
                    VerticalDivider(
                        modifier = modifier.size(28.dp),
                        color = Color.Gray
                    )

                    //phone number input
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = {
                            Text(
                                text = "Phone number",
                                color = Color(0xFFBBBBBB)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
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
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            //submit button
            Button(
                onClick = { /* Otp sending logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Send OTP",
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