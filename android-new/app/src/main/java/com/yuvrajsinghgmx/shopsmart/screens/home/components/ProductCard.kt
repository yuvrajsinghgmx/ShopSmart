package com.yuvrajsinghgmx.shopsmart.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@Composable
fun ProductCard (product: Product){
    Card (
        modifier = Modifier
            .width(150.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Column {
            AsyncImage(
                model = product.imageUrl.firstOrNull(),
                contentDescription = product.name,
                error = painterResource(R.drawable.error),
                placeholder = painterResource(R.drawable.error),
                fallback = painterResource(R.drawable.error),
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            ShopSmartTheme {
                Text(text = product.name,
                    Modifier.padding(start = 12.dp),
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ShopSmartTheme {
                Text(text = product.price,
                    Modifier.padding(start = 12.dp),
                    color = GreenPrimary,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ShopSmartTheme {
                Text(text = product.category,
                    Modifier.padding(start = 12.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

