package com.yuvrajsinghgmx.shopsmart.screens.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.viewmodel.HomeScreenViewModel
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(
    index: Int,
    navController: NavController
) {
    val itemsData = HomeScreenViewModel().itemsList.collectAsState().value[index]
    val listedSites = itemsData.listedSites
    val features = itemsData.features
    val scrollState = rememberLazyListState()

    // Outer Box to hold the scrollable content and fixed bottom buttons
    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable Column content inside LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp) // Space for the bottom buttons
        ) {
            // Top Bar Section
            item {
                TopAppBar(
                    title = {
                        Text(
                            text = "ShopSmart",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back_24px),
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*ToDo*/ }) {
                            Icon(
                                painter = painterResource(R.drawable.favorite_border_24px),
                                contentDescription = "Favorite"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Product Image Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(itemsData.image),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Product Title and Pricing
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = itemsData.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹ ${itemsData.currentPrice}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "₹ ${itemsData.currentPrice * 1.2}",
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "20% OFF",
                            color = Color.Green,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 16.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.star_rate_24px),
                                contentDescription = "Rating",
                                tint = Color(0xFFFFB800)
                            )
                            Text(
                                text = "4.5 (2.3k reviews)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Listed Sites Section
            items(listedSites.size) { index ->
                ListedSitesListLayout(
                    name = listedSites[index].name,
                    price = listedSites[index].price
                )
            }

            // Product Description and Features
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Product Description",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = itemsData.description,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Key Features",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            // Features List
            items(features.size) { index ->
                Text(
                    text = "• ${features[index]}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        // Bottom Row for Add to Cart and Buy Now Buttons
        Column(
            modifier = Modifier.fillMaxSize(), // Fill the available space
            verticalArrangement = Arrangement.SpaceBetween // Space the items evenly
        ) {
            // Other content can go here if needed

            Spacer(modifier = Modifier.weight(1f)) // This pushes the buttons to the bottom

            Row(
                modifier = Modifier.fillMaxWidth(), // Ensures the buttons take up the full width
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = { /* Add to cart logic */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp) // No bottom padding
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.shopping_cart_24px),
                            contentDescription = "Add to Cart"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Add to Cart", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = { /* Buy now logic */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp) // No bottom padding
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.payments_24px),
                            contentDescription = "Buy Now"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Buy Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ListedSitesListLayout(name: String = "Amazon", price: Float = 145.0f) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Card {
            Icon(
                painter = painterResource(R.drawable.shopping_cart_24px),
                contentDescription = "shopping cart Icon",
                modifier = Modifier.padding(10.dp)
            )
        }

        Column {
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
            onClick = { /*ToDo*/ },
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
fun ProductDetailsPreview() {
    // Create a preview-specific NavController
    val previewNavController = rememberNavController()
    ProductDetails(
        index = 0,
        navController = previewNavController
    )
}