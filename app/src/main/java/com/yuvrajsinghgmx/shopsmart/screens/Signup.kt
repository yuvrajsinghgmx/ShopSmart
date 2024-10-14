package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@Composable
fun SignUpScreen(
    onSignUpComplete: () -> Unit,
    onContinueWithEmail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "SKIP",
            fontFamily = FontFamily(Font(R.font.lexend_medium)),
            color = Color(0xFF888888),
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.clickable {
                onSignUpComplete()
            }.padding(top = 40.dp, end = 20.dp).align(Alignment.End),
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome to ShopSmart",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp),
                fontFamily = FontFamily(Font(R.font.lexend_semibold))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ShopSmart is your one-stop solution for all your shopping needs. Sign up today to enjoy exclusive deals, personalized offers, and more!",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.lexend_semibold)),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }


        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Shopping Bag",
                modifier = Modifier
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    onSignUpComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp).padding(start = 16.dp, end=16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Continue with Google",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.lexend_medium))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "OR",
                fontFamily = FontFamily(Font(R.font.lexend_regular)),
                color = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onContinueWithEmail()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email Logo",
                        tint = Color.Cyan,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Continue with Email",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.lexend_medium))
                    )
                }
            }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    ShopSmartTheme {
        SignUpScreen(onSignUpComplete = { }, onContinueWithEmail = { })
    }
}
