package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.viewmodel.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(index: Int){
    var itemsData = HomeScreenViewModel().itemsList[index]
    var listedSites = itemsData.listedSites
    var features = itemsData.features



    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "back arrow Icon")},
                title = {
                    Text(
                        text = "ShopSmart",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = {/*ToDo*/}
                    ){
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite Icon")
                    }
                }
            )
        }
    ) {innerPadding->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Card {
                    Image(
                        painter = painterResource(itemsData.image),
                        contentDescription = "Product Image"
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = itemsData.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                )

                Text(
                    text = "₹ ${itemsData.currentPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                LazyColumn(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                    items(listedSites.size) { index ->
                        ListedSitesListLayout(listedSites[index].name, listedSites[index].price)
                    }
                }

                Text(
                    text = itemsData.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    text = itemsData.description
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Key Features",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(features.size) { index ->
                        Text(
                            text = "- ${features[index]}"
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { /*ToDo*/ },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "shopping Cart"
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Add to Cart",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            FloatingActionButton(
                onClick = { /* ToDo */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.payments_24px),
                        contentDescription = "shopping Cart"
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Buy Now",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ListedSitesListLayout(name:String = "Amazon", price: Float = 145.0f){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly){
        Card(){
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "shopping cart Icon",
                modifier = Modifier.padding(10.dp)
            )
        }

        Column{
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "₹ $price",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.width(18.dp))

        Button(
            onClick = {/*ToDo*/},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "Compare Price"
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview(){
//    ProductDetails(HomeScreenViewModel().itemsList[0])
//    ListedSitesListLayout()
}