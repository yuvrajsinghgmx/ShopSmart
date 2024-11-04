package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.viewmodel.HomeScreenViewModel
import com.yuvrajsinghgmx.shopsmart.viewmodel.ItemsData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(viewModel: HomeScreenViewModel = HomeScreenViewModel()){
    val favorite = viewModel.itemsList.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "back Arrow")},
                title = {
                    Text(
                        text = "Favorites"
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.filter_list_24px), contentDescription = "sort Icon")
                    }
                }
            )
        }
    )
    {innerPadding->
        Column (Modifier.padding(innerPadding)){

        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            columns = GridCells.Fixed(2)
        ) {
            items(favorite.size){item->
                FavoriteCardLayout(favorite[item])
            }
        }
    }
}

@Composable
fun FavoriteCardLayout(itemsData: ItemsData){
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(160.dp)
            .aspectRatio(.95f)
            .padding(8.dp),
    ) {
        Column {
            Image(
                painter = painterResource(id = itemsData.image),
                contentDescription = "iPhone 13",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight(.45f)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = itemsData.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


//                    Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.padding(start = 5.dp),
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
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

//                    Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF00D4B4),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(1.dp),
                    text = "${itemsData.discount}% OFF",
                    color = Color(0xFF0B5AE3),
                    style = MaterialTheme.typography.labelMedium
                )

                Row {
                    Text(
                        text = "\u20B9 ${itemsData.currentPrice}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF0B5AE3)
                    )
                    Text(
                        text = itemsData.originalPrice.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(start = 4.dp)
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun FavPreview(){
    FavoriteScreen(HomeScreenViewModel())
}