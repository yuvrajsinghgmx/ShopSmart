package com.yuvrajsinghgmx.shopsmart.screens.support

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.dark

@Composable
fun FAQScreen() {
    // Sample FAQ data
    val faqs = listOf(
        "How do I manage my orders?" to  "You can manage orders by going to My Orders section.",
        "How do I update my account settings?" to  "Navigate to the Settings option to update your details.",
        "How do I manage my notifications?" to "To manage notifications, go to 'Settings,' select 'Notification Settings,' and customize your preferences.",
        "How do I start a guided meditation session?" to "You can start a session by selecting the 'Meditation' tab and following the guided steps.",
        "How do I join a support group?" to "Navigate to the 'Support Groups' section in the app and join any available group."
    )

    Column(modifier = Modifier.padding(top = 5.dp)) {
        faqs.forEach { (question, answer) ->
            FAQItem(question, answer)
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontFamily = FontFamily(Font(R.font.lexend_semibold)),
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.Black
                )
            }
            if (isExpanded) {
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    color = dark,
                    fontFamily = FontFamily(Font(R.font.lexend_regular)),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FAQScreenPreview(){
    FAQScreen()
}