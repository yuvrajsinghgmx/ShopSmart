package com.yuvrajsinghgmx.shopsmart.screens
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.core.net.toUri
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.LightGreyy
import com.yuvrajsinghgmx.shopsmart.ui.theme.YellowStarColor


@Composable
fun ProductDetails(product: Product, navController: NavController) {
    val context = LocalContext.current
    val shareIntent = remember {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this product: ${product.name}\n\n${product.description}"
            )
            type = "text/plain"
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Scroll entire screen
            .padding(top = 16.dp)
    ) {

        //top bar inside column
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            //Back Arrow
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )
            // text
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
            // share icon
            Icon(
                imageVector = Icons.Default.IosShare,
                contentDescription = "Share",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable {
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }
            )
        }
        // Image load
        ProductImageFromUrl(imageUrls = product.imageUrl)

        Spacer(modifier = Modifier.height(16.dp))

        // Product Name
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price & Type
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.price, style = MaterialTheme.typography.titleMedium,
                color = GreenPrimary
            )
            Text(
                text = product.category,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(color = Color.LightGray, shape = RoundedCornerShape(6.dp))
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Description
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // shop details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), colors = CardDefaults.cardColors(
                containerColor = LightGreyy
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(product.shopName, style = MaterialTheme.typography.titleSmall)


                    StarRating(rating = product.review.toFloat(), 5)

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.distance,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.map_placeholder),
                    contentDescription = "Map Placeholder",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(12.dp)),

                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // call and save buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${product.shopNumber}".toUri()
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call Shop")
                }
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "${product.name} saved!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(2.dp, GreenPrimary),

                ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // Replace with your image
                        contentDescription = "Save Product",
                        tint = GreenPrimary,

                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                    Text("Save Product", color = GreenPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        //view shop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Text(
                    text = "View Shop",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Store, // or your custom icon
                    contentDescription = "Store",
                    tint = GreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

    }

}

@Composable
fun ProductImageFromUrl(imageUrls: List<String>) {
    val listState = rememberLazyListState()

    val currentPage by remember {
        derivedStateOf {
            val visibleItem = listState.firstVisibleItemIndex
            if (visibleItem >= imageUrls.size) imageUrls.lastIndex else visibleItem
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()

                .height(250.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(imageUrls.size) { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls[index])
                        .crossfade(true)
                        .build(),
                    contentDescription = "Product Image $index",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .height(250.dp)
                )
            }
        }

        Text(
            text = "${currentPage + 1}/${imageUrls.size}",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

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
                modifier = Modifier.size(20.dp)
            )
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

