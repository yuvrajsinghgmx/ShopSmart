package com.yuvrajsinghgmx.shopsmart.screens.savedProducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SavedProductResponse
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.components.StarRating
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.LikeColor
import com.yuvrajsinghgmx.shopsmart.ui.theme.NavySecondary

@Composable
fun SavedProductCard(
    product: SavedProductResponse,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(170.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box{
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.productImage.firstOrNull()?: R.drawable.error)
                    .crossfade(true)
                    .build(),
                contentDescription = product.productName,
                placeholder = painterResource(R.drawable.error),
                error = painterResource(R.drawable.error),
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = { /* needs to be implemented*/ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = LikeColor
                )
            }
        }
        Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
            Spacer(modifier = modifier.height(1.dp))
            Text(
                text = product.productPrice.toString(),
                color = GreenPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.height(1.dp))
            Text(
                text = product.shopName,
                color = NavySecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
                StarRating(product.averageRating, 5,15.dp)

                Spacer(modifier = Modifier.width(20.dp))
//                Text(
//                    text = product.,
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
            }
        }
    }
}