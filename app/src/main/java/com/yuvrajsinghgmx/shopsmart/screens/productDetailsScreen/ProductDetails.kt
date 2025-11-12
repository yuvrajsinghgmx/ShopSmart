package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun ProductDetails(
    productId: Int,
    navController: NavController,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val productDetails by viewModel.productDetails.collectAsState()
    val isSaved = productDetails?.isFavorite ?: false
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    // Collect events (share, call, toast)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShareProduct -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out this product: ${event.product.name}\n\n${event.product.description}"
                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
                is UiEvent.CallShop -> {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${event.phoneNumber}".toUri()
                    }
                    context.startActivity(intent)
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fetch product details when composable is first launched
    LaunchedEffect(productId) {
        viewModel.fetchProductDetails(productId)
    }

    when {
        loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "Unknown error")
            }
        }

        productDetails != null -> {
            ProductDetailsUI(
                product = productDetails!!,
                onBack = { navController.popBackStack() },
                onShareClick = { viewModel.onShareClick() },
                onCallClick = { viewModel.onCallClick() },
                onSaveClick = { viewModel.onSaveClick() },
                isProductSaved = isSaved,
                navController = navController
            )
        }

        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Product not found")
            }
        }
    }
}