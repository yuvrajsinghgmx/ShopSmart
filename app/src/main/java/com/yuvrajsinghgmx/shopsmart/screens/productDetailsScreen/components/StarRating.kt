package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuvrajsinghgmx.shopsmart.ui.theme.YellowStarColor

@Composable
fun StarRating(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 20.dp,
    onClick: (() -> Unit)? = null
) {
    val fullStars = rating.toInt()
    val hasHalfStar = rating - fullStars >= 0.5f
    val emptyStars = maxStars - fullStars - if (hasHalfStar) 1 else 0
    // star print
    Row(
        modifier = Modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
    ) {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Full Star",
                tint = YellowStarColor,
                modifier = Modifier.size(starSize)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = "Half Star",
                tint = YellowStarColor,
                modifier = Modifier.size(starSize)
            )
        }
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Empty Star",
                tint = YellowStarColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}