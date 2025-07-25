package com.shopsmart.new_android.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopsmart.new_android.ui.theme.ShopSmartTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopSmartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Shield Icon
        Image(
            painter = painterResource(id = R.drawable.ic_shield_check), // Replace with your actual drawable resource
            contentDescription = "Security Shield Icon",
            modifier = Modifier
                .size(120.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Welcome Text
        Text(
            text = "Welcome to ShopSmart",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle Text
        Text(
            text = "Enter your mobile number to continue",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Phone Number Input Field
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle phone number input */ },
            label = { Text("Phone number") },
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("+1", modifier = Modifier.padding(start = 8.dp)) // Country code
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.width(1.dp).height(24.dp).padding(vertical = 4.dp))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send OTP Button
        Button(
            onClick = { /* Handle send OTP action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Send OTP", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Terms and Privacy Policy Text
        Text(
            text = "By continuing, you agree to our Terms & Privacy Policy",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ShopSmartTheme {
        LoginScreen()
    }
}
