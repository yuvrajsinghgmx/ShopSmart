package com.yuvrajsinghgmx.shopsmart.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(onSubmit: (fullName: String) -> Unit) {
    val (fullName, setFullName) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Complete your profile")
        OutlinedTextField(value = fullName, onValueChange = setFullName, label = { Text("Full name") })
        Button(onClick = { onSubmit(fullName) }) {
            Text("Continue")
        }
    }
}


