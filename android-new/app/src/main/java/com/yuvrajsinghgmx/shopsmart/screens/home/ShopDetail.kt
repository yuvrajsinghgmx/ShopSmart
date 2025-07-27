package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTheme

@Composable
fun ShopDetail(sharedViewModel: SharedShopViewModel) { // ✅ Remove viewModel() to avoid new instance
    val selectedShop = sharedViewModel.selectedShop.collectAsState().value

    if (selectedShop == null) {
        // ✅ Show fallback if no shop is selected
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No shop selected", style = MaterialTheme.typography.titleLarge)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ✅ Shop Image
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AsyncImage(
                    model = selectedShop.imageUrl.firstOrNull(),
                    contentDescription = selectedShop.shopName,
                    error = painterResource(R.drawable.error), // ✅ Add placeholder image in drawable
                    placeholder = painterResource(R.drawable.error),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Shop Name
            ShopSmartTheme {
                Text(
                    text = selectedShop.shopName,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Category
            ShopSmartTheme {
                Text(
                    text = "Category: ${selectedShop.category}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Distance
            ShopSmartTheme {
                Text(
                    text = "Distance: ${selectedShop.distance}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}