package com.yuvrajsinghgmx.shopsmart.sharedComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun SearchBarComposable(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Search shops or products...", color = Color.Gray, fontSize = 18.sp) },
        singleLine = true,
        leadingIcon = {
            IconButton(onClick = { onSearch() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.DarkGray)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            cursorColor = GreenPrimary
        ),
        shape = RoundedCornerShape(12.dp)
    )
}