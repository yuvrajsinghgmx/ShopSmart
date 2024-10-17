package com.yuvrajsinghgmx.shopsmart.viewmodel

import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.R


// data class for items of lazyRow inside homePage
data class ItemsData(
    val image: Int,
    val name: String,
    val platform: String, // platform(website) where it is listed
    val rating: Float,
    val discount: Int,
    val originalPrice: Int,
    val currentPrice: Int
)


class HomeScreenViewModel: ViewModel(){
    // Demo data
    val itemsList = listOf(
        ItemsData(R.drawable.shopinterior, "Phone", "flipkart", 4.5f, 10, 500, 480),
        ItemsData(R.drawable.shopinterior, "Phone", "flipkart", 4.5f, 10, 500, 480),
        ItemsData(R.drawable.shopinterior, "Phone", "flipkart", 4.5f, 10, 500, 480),
        ItemsData(R.drawable.shopinterior, "Phone", "flipkart", 4.5f, 10, 500, 480),
    )
}