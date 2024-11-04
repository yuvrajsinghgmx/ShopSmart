package com.yuvrajsinghgmx.shopsmart.screens.policies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.dark
import com.yuvrajsinghgmx.shopsmart.ui.theme.headingColor
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

@Composable
fun TermsAndConditionsScreen(
    onBackClick: () -> Unit,
) {
    val scrollState= rememberScrollState()
    val context= LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("If you have any questions or concerns about these Terms and Conditions, please contact us at ")

        // Annotate and style the email part
        withStyle(style = SpanStyle(color = Color(0xFF1098F7), textDecoration = TextDecoration.Underline)) {
            append("yuvrajsinghgmx@gmail.com")
        }
        append(".")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title - "Terms and Conditions"
        Text(
            text = "Terms and Conditions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = headingColor,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
           textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Last Updated
        Text(
            text = "Last Updated: 16th October, 2024",
            fontSize = 14.sp,
            color = dark,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = stringResource(R.string.heading_text),
            fontSize = 14.sp,
            color = dark,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "1. Acceptance of Terms",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top=5.dp)
        )

        Text(
            text = stringResource(R.string.acceptance_terms),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
          modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "2. Use of the App",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
        )
        Text(
            text = stringResource(R.string.uses_of_terms),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "3. Account Creation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
        )
        Text(
            text = stringResource(R.string.account_creation_text) ,
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "4. Privacy",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
        )
        Text(
            text = stringResource(R.string.privacy_text),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "5. Third-Party Services",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.thirdparty_services),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Purchases and Payments
        Text(
            text = "6. Purchases and Payments",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.purchases_payment),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Returns and Refunds
        Text(
            text = "7. Returns and Refunds",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.return_refund),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Limitation of Liability
        Text(
            text = "8. Limitation of Liability",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.liability),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        // Intellectual Property
        Text(
            text = "9. Intellectual Property",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold))
        )
        Text(
            text = "All content, logos, and designs on ShopSmart are owned by us or licensed to us. You may not use, reproduce, or distribute any content without our explicit permission.",
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))


        Text(
            text = "10. Changes to the Terms",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
        )
        Text(
            text = stringResource(R.string.changes_terms),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        // Termination
        Text(
            text = "11. Termination",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
        )
        Text(
            text = stringResource(R.string.termination),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        // Governing Law
        Text(
            text = "12. Governing Law",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold))
        )
        Text(
            text = stringResource(R.string.governing_law),
            fontSize = 11.sp,
            color = dark,
            textAlign = TextAlign.Justify,
            fontFamily = FontFamily(Font(R.font.lexend_semibold)),
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "13. Contact Us",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.lexend_semibold))
        )

        SelectionContainer {
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    val emailStartIndex = annotatedString.indexOf("yuvrajsinghgmx@gmail.com")
                    if (offset in emailStartIndex until (emailStartIndex + "yuvrajsinghgmx@gmail.com".length)) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:yuvrajsinghgmx@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Inquiry about ShopSmart")
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.padding(top = 5.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    color = dark,
                    textAlign = TextAlign.Justify,
                    fontFamily = FontFamily(Font(R.font.lexend_semibold))
                )
            )
        }

    }
}


@Preview(showBackground = true,showSystemUi = true)
@Composable
fun DefaultPreview() {
    TermsAndConditionsScreen {

    }
}

