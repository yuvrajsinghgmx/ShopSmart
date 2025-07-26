package com.yuvrajsinghgmx.shopsmart.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@Composable
fun ShopCard(shop: Shop) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height((95.dp))
                    .padding(start = 5.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                AsyncImage(
                    model = shop.imageUrl.firstOrNull(),
                    contentDescription = shop.shopName,
                    error = painterResource(R.drawable.error),
                    placeholder = painterResource(R.drawable.error),
                    fallback = painterResource(R.drawable.error),
                    contentScale = ContentScale.Crop,
                )
            }
            Column (
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ){
                ShopSmartTheme {
                    Text(text = shop.shopName,
                        fontSize = 18.sp)
                }
                ShopSmartTheme {
                    Text(text = shop.category,
                        color = Color.Gray
                    )
                }
                ShopSmartTheme {
                    Text(text = shop.distance,
                        color = GreenPrimary
                    )
                }
            }
        }
    }
}