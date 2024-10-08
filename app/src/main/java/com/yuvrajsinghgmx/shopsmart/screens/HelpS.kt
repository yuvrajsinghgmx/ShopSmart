package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.yuvrajsinghgmx.shopsmart.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpS(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "FAQs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // List of FAQs
                LazyColumn {
                    items(faqs) { faq ->
                        FAQItem(faq.question, faq.answer)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Contact Us Section
                Text(
                    text = "Need more help?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Contact Us",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Gmail

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.gmail),
                        contentDescription = "Gmail",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openLink(context, "mailto:example@gmail.com")
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "gmail",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            openLink(context, "mailto:example@gmail.com")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.linkedin),
                        contentDescription = "LinkedIn",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openLink(context, "https://linkedin.com/in/yuvrajsinghgmx")
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LinkedIn Profile",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            openLink(context, "https://linkedin.com/in/yuvrajsinghgmx")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = "LinkedIn",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openLink(context, "https://instagram.com/yuvrajsinghgmx")
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Instagram Profile",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            openLink(context, "https://instagram.com/yuvrajsinghgmx")
                        }
                    )
                }


            }
        }
    )
}
fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    // Try opening the LinkedIn app
    if (url.contains("linkedin.com")) {
        intent.setPackage("com.linkedin.android")
    }

    // Fallback to browser if the app isn't installed
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Open in browser if app is not available
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}


@Composable
fun FAQItem(question: String, answer: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = question, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = answer, color = Color.Gray)
    }
}

// Example FAQ data
val faqs = listOf(
    FAQ("How do I manage my orders?", "You can manage orders by going to My Orders section."),
    FAQ("How do I update my account settings?", "Navigate to the Settings option to update your details.")
)

data class FAQ(val question: String, val answer: String)




