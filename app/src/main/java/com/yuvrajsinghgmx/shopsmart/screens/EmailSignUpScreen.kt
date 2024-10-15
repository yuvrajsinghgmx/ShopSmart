package com.yuvrajsinghgmx.shopsmart.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.dark

@Composable
fun EmailSignUpScreen(onSignUpComplete: () -> Unit, onBackButtonClicked:()->Unit) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Icon(
            modifier = Modifier.clickable {
                onBackButtonClicked()
            }
                .padding(top = 30.dp),
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Image(
                painter = painterResource(R.drawable.logo1),
                contentDescription = "Logo"
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Continue to ShopSmart \n with your Email",
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.lexend_semibold)),
                textAlign = TextAlign.Center,
                color = dark
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = { Text("Full Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        //handle onClickListener
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007AFF))
                ) {
                    Text(
                        text = "Continue",
                        fontFamily = FontFamily(Font(R.font.lexend_medium)),
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        color = Color(0x9A888888),
                        thickness = 0.6.dp,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "OR",
                        fontFamily = FontFamily(Font(R.font.lexend_regular)),
                        color = Color(0xFF888888),
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )

                    HorizontalDivider(
                        color = Color(0x9A888888),
                        thickness = 0.6.dp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        onSignUpComplete()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(3.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Row {
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

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 30.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info Icon",
                            tint = Color.Cyan,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 3.dp)
                        )

                        Text(
                            text = "By continuing, you agree to our",
                            fontFamily = FontFamily(Font(R.font.lexend_regular)),
                            color = Color(0xFF888888),
                        )

                        Text(
                            text = "Terms of Use",
                            fontFamily = FontFamily(Font(R.font.lexend_regular)),
                            color = Color.Blue,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .clickable {
                                    Toast
                                        .makeText(
                                            context,
                                            "Terms of Use Clicked",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                .padding(start = 5.dp)
                                .fillMaxWidth()

                        )
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SingUpScreenPreview() {
  EmailSignUpScreen(onSignUpComplete = {}, onBackButtonClicked = {})
}
