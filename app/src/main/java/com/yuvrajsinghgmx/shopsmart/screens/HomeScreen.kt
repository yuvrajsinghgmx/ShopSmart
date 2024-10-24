package com.yuvrajsinghgmx.shopsmart.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = HomeScreenViewModel(), navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var showExitDialog by remember { mutableStateOf(false) }
    var myItems = viewModel.itemsList

    // Handle back press for this screen
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    // Exit Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit App") },
            text = { Text("Do you want to exit the app?") },
            confirmButton = {
                TextButton(onClick = {
                    // Get the activity context and finish it
                    (navController.context as? Activity)?.finish()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = searchQuery,
                onValueChange  = {searchQuery = it},
                shape = RoundedCornerShape(20.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search Icon") },
                singleLine = true,
                placeholder = { Text("Search for products") }
            )

            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.baseline_keyboard_voice_24), contentDescription = "mike Icon")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box{
            Image(
                painter = painterResource(R.drawable.shopinterior),
                contentDescription = "",
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToScale(0.7f, 0.7f, 0.7f, 1f)
                    }
                )
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Welcome to ShopSmart",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Your one-stop shop for everything",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Featured",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(myItems.size){
                items->
                CardLayout(myItems[items])
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Featured Products",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )

        Text(
            text = "At ShopSmart, we bring you the best deals on a wide range of products. From the latest electronics to fashionable clothing, we have everything you need at unbeatable prices. Our user-friendly app..."
        )
    }
}

// layout for card in LazyRow product view
@Composable
fun CardLayout(itemsData: ItemsData){
    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.width(160.dp).aspectRatio(.7f).padding(8.dp)) {
        Column {
            Image(
                painter = painterResource(id = itemsData.image), // Replace with your image resource or use Coil for URL
                contentDescription = "iPhone 13",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight(.45f)
            )

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = itemsData.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = itemsData.platform,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

//                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.padding(start = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_star_24),
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = itemsData.rating.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

//                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.background(
                            color = Color(0xFFFFA500),
                            shape = RoundedCornerShape(8.dp)
                            )
                            .padding(1.dp),
                        text = "${itemsData.discount}% OFF",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelMedium
                    )
                    
                    Row {
                        Text(
                            text = "\u20B9 ${itemsData.currentPrice}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Red
                        )
                        Text(
                            text = itemsData.originalPrice.toString(),
                            style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Bottom).padding(start = 4.dp)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { /* Handle like action */ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color.Gray,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = "Like",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun HomePagePreview(){
//    HomeScreen()
//}
