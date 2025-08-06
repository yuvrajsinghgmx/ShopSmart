package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuvrajsinghgmx.shopsmart.viewmodel.ProductViewModel
import com.yuvrajsinghgmx.shopsmart.modelclass.Category

@Composable
fun CustomerHomeScreen(viewModel: ProductViewModel = viewModel()) {
    val categories = Category.values().toList()
    val selectedCategory by viewModel.selectedCategory
    val products by viewModel.filteredProducts

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Trending Products", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow {
            items(categories) { category ->
                Button(
                    onClick = { viewModel.selectCategory(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (category == selectedCategory)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                ) {
                    Text(text = category.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(products) { product ->
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

