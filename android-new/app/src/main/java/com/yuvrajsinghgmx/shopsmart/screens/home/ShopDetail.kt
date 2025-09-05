package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.home.components.AllProductCard
import com.yuvrajsinghgmx.shopsmart.screens.home.components.FeaturedProductCard
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun ShopDetail(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedShopViewModel,
    onBackClick: () -> Unit = {},
    navController: NavController
) {
    val selectedShop = sharedViewModel.selectedShop.collectAsState().value
    val state = viewModel.state.value



    if (selectedShop == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No shop selected", style = MaterialTheme.typography.titleLarge)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedShop.imageUrl.firstOrNull())
                            .build(),
                        contentDescription = selectedShop.shopName,
                        placeholder = painterResource(R.drawable.error),
                        error = painterResource(R.drawable.error),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )


// Back Button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp)
                            .background(Color(0x66FFFFFF), CircleShape)
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

// Shop Info Left-Middle (below back, above rating)
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            LabelChip(text = selectedShop.category, backgroundColor = GreenPrimary)
                            LabelChip(
                                text = selectedShop.distance,
                                backgroundColor = Color(0xFF4DB6F7)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = selectedShop.shopName,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 26.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF888888))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clickable{ navController.navigate("reviewScreen/shop/${selectedShop.shopId}") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700)
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.StarHalf,
                        contentDescription = "Half Star",
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("4.8", fontSize = 16.sp)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionItem(icon = Icons.Filled.LocationOn, label = "Directions")
                    ActionItem(icon = Icons.Filled.Call, label = "Call")
                    ActionItem(icon = Icons.Filled.Share, label = "Share")
                }
            }
            item {
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
            }
            item {
                InfoRow(
                    icon = Icons.Filled.LocationOn,
                    label = "Address",
                    value = "123 Market Street, Downtown"
                )
                InfoRow(icon = Icons.Filled.Call, label = "Phone", value = "+1 (555) 123-4567")
                InfoRow(
                    icon = Icons.Filled.AccessTime,
                    label = "Hours",
                    value = "Mon-Sat: 8:00 AM - 9:00 PM"
                )
            }
            item {
                Text(
                    text = "Your neighbourhood fresh grocery store offering fresh produce, organic foods and " +
                            "local products at competitive prices.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            item {
                Text(
                    text = "Featured Products",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)

                ) {
                    items(state.products) { product ->
                        FeaturedProductCard(product)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "All Products",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            items(state.products.chunked(2)) {
                    rowProducts -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowProducts.forEach {
                        product ->
                    Box(modifier = Modifier.weight(1f)) {
                        AllProductCard(product)
                    }
                }
                // Fill remaining space if odd number of products
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
            }
            item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        Text(
                            text = "Call Shop",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.heart_svgrepo_com),
                                contentDescription = "Save button",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Save Shop",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                    }
                }

            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun LabelChip(text: String, backgroundColor: Color) {
    Surface(color = backgroundColor, shape = RoundedCornerShape(20.dp)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF2121D2),
            modifier = Modifier.size(28.dp)
        )
        Text(text = label, fontSize = 14.sp, color = Color(0xFF2121D2))
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF2121D2),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

