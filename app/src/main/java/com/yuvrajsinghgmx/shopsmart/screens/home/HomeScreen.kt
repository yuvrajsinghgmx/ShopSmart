package com.yuvrajsinghgmx.shopsmart.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.VoiceTextParser
import com.yuvrajsinghgmx.shopsmart.viewmodel.HomeScreenViewModel
import com.yuvrajsinghgmx.shopsmart.viewmodel.ItemsData

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = HomeScreenViewModel(),
    navController: NavController, voiceToTextParser: VoiceTextParser
) {
    var searchQuery by remember { mutableStateOf("") }
    var showExitDialog by remember { mutableStateOf(false) }
    val myItems = viewModel.itemsList.collectAsState()
    val scrollState = rememberScrollState()
    var placeholderText by remember { mutableStateOf("ShopSmart") }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Add back handler
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    placeholderText = if (isFocused) "Search for products" else "ShopSmart"

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit App") },
            text = { Text("Do you want to exit the app?") },
            confirmButton = {
                TextButton(onClick = { (navController.context as? Activity)?.finish() }) {
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

        var canRecord by remember{
            mutableStateOf(false)
        }

        val recordAudioLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                canRecord = isGranted
            }
        )

        LaunchedEffect(key1 = recordAudioLauncher){
            recordAudioLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }

        val state by voiceToTextParser.state.collectAsState()

        LaunchedEffect(state.spokenText){
            if(state.spokenText.isNotBlank()){
                searchQuery = state.spokenText
            }
        }

        Column(Modifier.padding(16.dp, bottom = 0.dp)) {
            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(4.dp),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    interactionSource = interactionSource,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    singleLine = true,
                    placeholder = { Text(placeholderText) },
                    colors = TextFieldDefaults.colors(
//                  Adding colors
                    )
                )
                IconButton(onClick = {
                    if(state.isSpeaking){
                        voiceToTextParser.stopListening()
                    }else{
                        voiceToTextParser.startListening()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Search"
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Search Bar

            // Welcome Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.shopinterior),
                    contentDescription = "Shop Interior",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                        setToScale(0.7f, 0.7f, 0.7f, 1f)
                    })
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        "Welcome to ShopSmart",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Your one-stop shop for everything",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Featured Section
            Text(
                "Featured",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(myItems.value.size) { index ->
                    CardLayout(myItems.value[index], index, navController)
                }
            }

            // Description Section
            Text(
                "At ShopSmart, we bring you the best deals on a wide range of products. From the latest electronics to fashionable clothing, we have everything you need at unbeatable prices. Our user-friendly app...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CardLayout(itemsData: ItemsData, index: Int, navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(180.dp)
            .aspectRatio(0.70f)
            .padding(8.dp)
            .shadow(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { navController.navigate("productDetails/$index") }
    ) {
        Box(
            Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = itemsData.image),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    itemsData.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    itemsData.platform,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_star_24),
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${itemsData.rating}",
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${itemsData.discount}% OFF",
                    color = Color.White,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        "\u20B9 ${itemsData.currentPrice}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "\u20B9 ${itemsData.originalPrice}",
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomePagePreview(){
//    context = LocalContext.current
//    HomeScreen()
//}
