package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product

@Composable
fun ProductDetails(
    product: Product,
    navController: NavController,
    viewModel: ProductDetailsViewModel = viewModel()
) {
    val isSaved by viewModel.isProductSaved
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShareProduct -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, "Check out this product: ${event.product.name}\n\n${event.product.description}")
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
    ProductDetailsUI(
        product = product,
        onBack = { navController.popBackStack() },
        onShareClick = { viewModel.onShareClick(product) },
        onCallClick = { viewModel.onCallClick(product.shopNumber) },
        onSaveClick = { viewModel.onSaveClick(product) },
        isProductSaved = isSaved
    )
}