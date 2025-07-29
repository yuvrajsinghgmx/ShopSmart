package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary

@Composable
fun ShopDetail(
    sharedViewModel: SharedShopViewModel,
    onBackClick: () -> Unit = {}
) {
    val selectedShop = sharedViewModel.selectedShop.collectAsState().value

    if (selectedShop == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No shop selected", style = MaterialTheme.typography.titleLarge)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
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
                        LabelChip(text = selectedShop.distance, backgroundColor = Color(0xFF4DB6F7))
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF888888))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) {
                    Icon(Icons.Filled.Star, contentDescription = "Star", tint = Color(0xFFFFD700))
                }
                Icon(
                    Icons.AutoMirrored.Filled.StarHalf,
                    contentDescription = "Half Star",
                    tint = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("4.8", fontSize = 16.sp)
            }

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

            HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

            InfoRow(icon = Icons.Filled.LocationOn, label = "Address", value = "123 Market Street, Downtown")
            InfoRow(icon = Icons.Filled.Call, label = "Phone", value = "+1 (555) 123-4567")
            InfoRow(icon = Icons.Filled.AccessTime, label = "Hours", value = "Mon-Sat: 8:00 AM - 9:00 PM")
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
        Icon(icon, contentDescription = label, tint = Color(0xFF2121D2), modifier = Modifier.size(28.dp))
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
        Icon(icon, contentDescription = label, tint = Color(0xFF2121D2), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}
