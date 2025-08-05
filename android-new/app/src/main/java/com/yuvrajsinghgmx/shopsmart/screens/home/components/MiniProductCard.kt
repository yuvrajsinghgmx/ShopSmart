package com.yuvrajsinghgmx.shopsmart.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.modelclass.Product

@Composable
fun MiniProductCard(product: Product, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = product.imageUrl.firstOrNull() ?: "",
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(82.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            product.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            product.price,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp
        )
    }
}
