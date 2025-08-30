package com.yuvrajsinghgmx.shopsmart.screens.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun RatingSelector(selected:Int, onSelected:(Int)->Unit) {
    Row {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= selected) Icons.Default.Star
                else Icons.Default.StarOutline,
                contentDescription = "start $index",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onSelected(index)
                    })
        }
    }
}