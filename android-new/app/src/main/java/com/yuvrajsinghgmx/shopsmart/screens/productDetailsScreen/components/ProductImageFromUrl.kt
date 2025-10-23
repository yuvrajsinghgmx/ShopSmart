package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProductImageFromUrl(imageUrls: List<String>) {
    val images = remember(imageUrls) { imageUrls.distinct() }
    val listState = rememberLazyListState()
    val currentPage by remember {
        derivedStateOf { minOf(listState.firstVisibleItemIndex, images.lastIndex) }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp

        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            itemsIndexed(images) { index, url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .crossfade(300)
                        .build(),
                    contentDescription = "Product Image ${index + 1} of ${images.size}",
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.error),
                    placeholder = painterResource(R.drawable.error),
                    modifier = Modifier
                        .width(screenWidth - 60.dp)
                        .height(264.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        // Image indicator overlay
        ImageIndicator(currentPage, images.size)
    }
}

@Composable
private fun ImageIndicator(currentPage: Int, total: Int) {
    if (total > 1) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = "${currentPage + 1}/$total",
                color = Color.White,
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}