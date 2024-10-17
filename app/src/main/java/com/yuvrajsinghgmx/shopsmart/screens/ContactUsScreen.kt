package com.yuvrajsinghgmx.shopsmart.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R

@Composable
fun ContactUsScreen() {
    val context = LocalContext.current
    val data = listOf(
        ContactUsData(R.drawable.customer_care, "Customer Services") {
            Toast.makeText(
                context,
                "Coming soon",
                Toast.LENGTH_SHORT
            ).show()
        },

        ContactUsData(R.drawable.whatsapp, "Whatsapp") {

        },
        ContactUsData(R.drawable.linkedin, "Linkedin") {
            openLink(context, "https://linkedin.com/in/yuvrajsinghgmx")
        },
        ContactUsData(R.drawable.instagram, "Instagram") {
            openLink(context, "https://instagram.com/yuvrajsinghgmx")
        },
        ContactUsData(R.drawable.fb, "Facebook") {},
        ContactUsData(R.drawable.gmail, "Gmail") {
            openLink(context, "mailto:yuvrajsinghgmx@gmail.com")
        },

        )

    Column(modifier = Modifier.padding(bottom = 15.dp, top = 10.dp)) {
        data.forEach {
            ContactUsItem(contactUsData = it)
        }
    }
}

data class ContactUsData(
    val icon: Int,
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun ContactUsItem(contactUsData: ContactUsData) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 15.dp)
            .clickable {
                contactUsData.onClick()
            },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(contactUsData.icon),
                contentDescription = contactUsData.title,
                modifier = Modifier.padding(start = 24.dp, end = 22.dp)
            )

            Text(
                text = contactUsData.title,
                fontFamily = FontFamily(Font(R.font.lexend_semibold)),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactUsScreenPreview() {
    ContactUsScreen()
}