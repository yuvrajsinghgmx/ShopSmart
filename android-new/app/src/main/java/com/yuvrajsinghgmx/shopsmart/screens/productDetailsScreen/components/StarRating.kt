package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuvrajsinghgmx.shopsmart.ui.theme.YellowStarColor

@Composable
fun StarRating(rating: Float, maxStars: Int = 5) {
    val fullStars = rating.toInt()
    val hasHalfStar = rating - fullStars >= 0.5f
    val emptyStars = maxStars - fullStars - if (hasHalfStar) 1 else 0
    // star print
    Row {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Full Star",
                tint = YellowStarColor,
                modifier = Modifier.size(20.dp)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = "Half Star",
                tint = YellowStarColor,
                modifier = Modifier.size(20.dp))
        }
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Empty Star",
                tint = YellowStarColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}