package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.viewmodel.HomeScreenViewModel
import com.yuvrajsinghgmx.shopsmart.viewmodel.ItemsData
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(

    navController: NavController? = null ,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val itemsList = viewModel.itemsList.collectAsState().value
    val favoriteItems = itemsList.filter { it.favorite }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Custom Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController?.popBackStack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list_24px),
                        contentDescription = "Filter",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Grid of Favorite Items
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(favoriteItems.size) { index ->
                    val item = favoriteItems[index]
                    FavoriteCardLayout(
                        itemsData = item,
                        onClick = {/*TODO*/}
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCardLayout(
    itemsData: ItemsData,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF8F8F8))
        ) {
            // Product Image
            Image(
                painter = painterResource(id = itemsData.image),
                contentDescription = itemsData.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
            )

            // Product Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Product Name
                Text(
                    text = itemsData.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_star_24),
                        contentDescription = "Rating",
                        tint = Color(0xFF0B5AE3),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = itemsData.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF0B5AE3),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Discount Badge
                Text(
                    text = "${itemsData.discount}% OFF",
                    color = Color(0xFF0B5AE3),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(
                            color = Color(0xFF00D4B4).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "₹ ${itemsData.currentPrice}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFF0B5AE3),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "₹${itemsData.originalPrice}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        ),
                        modifier = Modifier.padding(start = 8.dp, bottom = 1.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavPreview() {
    FavoriteScreen()
}