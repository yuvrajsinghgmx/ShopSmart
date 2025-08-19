package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.ui.theme.BackgroundDark
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary
import com.yuvrajsinghgmx.shopsmart.ui.theme.PurpleGrey40
import com.yuvrajsinghgmx.shopsmart.ui.theme.PurpleGrey80

/*@Composable
fun OnBoardingScreen(onFinish: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Onboarding Screen", modifier = Modifier.size(200.dp))

        IconButton(
            onClick = { onFinish() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}*/

@Composable
fun OnBoardingScreen(onFinish: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Complete Your Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BackgroundDark,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )

        //// Profile Photo Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .clickable { /* Handle photo selection */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Add Photo",
                tint = NavySecondary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "Add Photo",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = PurpleGrey40,
            modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
        )

        // Full Name Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Full Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = {
                    Text(
                        text = "Enter your full name",
                        color = PurpleGrey80
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFDDD6FE),
                    focusedBorderColor = Color(0xFF6C5CE7),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Role Selection
        Text(
            text = "Select Your Role",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = BackgroundDark,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleCard(
                role = UserRole.CUSTOMER,
                isSelected = selectedRole == UserRole.CUSTOMER,
                onClick = { selectedRole = UserRole.CUSTOMER },
                modifier = Modifier.weight(1f)
            )

            RoleCard(
                role = UserRole.SHOP_OWNER,
                isSelected = selectedRole == UserRole.SHOP_OWNER,
                onClick = { selectedRole = UserRole.SHOP_OWNER },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Email (Optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "Enter your email",
                        color = PurpleGrey80
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFDDD6FE),
                    focusedBorderColor = Color(0xFF6C5CE7),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Text(
                text = "You can add this later",
                fontSize = 14.sp,
                color = PurpleGrey40,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Continue Button
        Button(
            onClick = { onFinish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom text
        Text(
            text = "You can switch roles later from settings",
            fontSize = 14.sp,
            color = PurpleGrey40,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) Color(0xFF6C5CE7) else Color(0xFFDDD6FE)
    val backgroundColor = if (isSelected) Color(0xFFF8F7FF) else Color.White

    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = role.icon,
                contentDescription = role.title,
                tint = NavySecondary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = role.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BackgroundDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class UserRole(val title: String, val icon: ImageVector) {
    CUSTOMER("Customer", Icons.Default.ShoppingBag),
    SHOP_OWNER("Shop Owner", Icons.Default.Store)
}

/*@Preview(showBackground = true)
@Composable
fun CompleteProfileScreenPreview() {
    MaterialTheme {
        OnBoardingScreen()
    }
}*/
