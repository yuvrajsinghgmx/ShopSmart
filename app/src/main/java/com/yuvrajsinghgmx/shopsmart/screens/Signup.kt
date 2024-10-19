package com.yuvrajsinghgmx.shopsmart.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme
import com.yuvrajsinghgmx.shopsmart.ui.theme.dark
import com.yuvrajsinghgmx.shopsmart.ui.theme.subHeadingColor

@Composable
fun SignUpScreen(
    onSignUpComplete: () -> Unit,
    onContinueWithEmail: () -> Unit,
    onTermsAndConditionsClick: () -> Unit
) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("By continuing, you agree to our ")

        // Add "Terms of Use" as clickable text
        pushStringAnnotation(tag = "terms", annotation = "terms_of_use")
        withStyle(style = SpanStyle(color = Color.Blue, fontSize = 15.sp)) {
            append("Terms of Use")
        }
        pop() // End the clickable part
    }

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
            modifier = Modifier
                .clickable {
                    onSignUpComplete()
                }
                .padding(top = 30.dp, end = 20.dp)
                .align(Alignment.End),
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 16.dp, end = 16.dp),
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
                color = subHeadingColor,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.lexend_semibold)),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }


        Spacer(modifier = Modifier.height(20.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onSignUpComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row(
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
                        color = dark,
                        fontFamily = FontFamily(Font(R.font.lexend_medium))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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



            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onContinueWithEmail()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
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
                        color = dark,
                        fontFamily = FontFamily(Font(R.font.lexend_medium))
                    )
                }
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
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info Icon",
                    tint = Color.Cyan,
                    modifier = Modifier
                        .size(24.dp)

                )

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.lexend_regular)),
                        color = Color(0xFF888888)
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth(),
                    onClick = { offset ->
                        // Check if the user clicked on the "Terms of Use" part
                        annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                            .firstOrNull()?.let {
                                // Perform the action for "Terms of Use" click
                                onTermsAndConditionsClick()
                            }
                    }
                )
            }
        }



    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    ShopSmartTheme {
        SignUpScreen(onSignUpComplete = { }, onContinueWithEmail = { }, onTermsAndConditionsClick = {})
    }
}
