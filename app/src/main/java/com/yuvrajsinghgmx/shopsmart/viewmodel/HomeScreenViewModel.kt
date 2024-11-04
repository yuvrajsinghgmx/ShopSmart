package com.yuvrajsinghgmx.shopsmart.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.test.filter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn


data class ListedSite(
    val name: String,
    val price: Float,
)

// data class for items of lazyRow inside homePage
data class ItemsData(
    val image: Int,
    val name: String,
    val platform: String, // platform(website) where it is listed
    val rating: Float,
    val discount: Int,
    val originalPrice: Int,
    val currentPrice: Int,
    val listedSites : List<ListedSite>,
    val features : List<String> ,
    val description: String,
    var favorite: Boolean = false
)


class HomeScreenViewModel: ViewModel(){

    // Demo data
    private val _itemsList = MutableStateFlow<List<ItemsData>>(listOf(
        ItemsData(R.drawable.shopinterior, "Phone1", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        ),
        ItemsData(R.drawable.shopinterior, "Phone2", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        ),
        ItemsData(R.drawable.shopinterior, "Phone3", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        ),
        ItemsData(R.drawable.shopinterior, "Phone4", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        ) ,
        ItemsData(R.drawable.shopinterior, "Phone5", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        ),
        ItemsData(R.drawable.shopinterior, "Phone6", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability.."
        )
    )
    )

    val itemsList: StateFlow<List<ItemsData>> = _itemsList.asStateFlow()

    val favorite = emptyList<ItemsData>()



    fun changeFavorite(index: Int) {
        favorite + _itemsList.value[index]
//        _itemsList.value = _itemsList.value.mapIndexed { it, data->
//            when(it){
//                index-> data.copy(favorite = true)
//                else->data
//            }
//        }
    }
}

