package com.yuvrajsinghgmx.shopsmart.viewmodel
import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


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
    val favorite: Boolean
)


class HomeScreenViewModel: ViewModel(){

    // Demo data
    //NOTE :fetch and use realtime user data instead of dummy data to keep the changes saved!
    private val _itemsList = MutableStateFlow(listOf(
        ItemsData(R.drawable.shopinterior, "Phone1", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            false
        ),
        ItemsData(R.drawable.shopinterior, "Phone2", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            true
        ),
        ItemsData(R.drawable.shopinterior, "Phone3", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            true
        ),
        ItemsData(R.drawable.shopinterior, "Phone4", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            false
        ) ,
        ItemsData(R.drawable.shopinterior, "Phone5", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            false
        ),
        ItemsData(R.drawable.shopinterior, "Phone6", "flipkart", 4.5f, 10, 500, 480,
            listOf(ListedSite("Amazon", 150.0f), ListedSite("Flipkart", 300.0f)), listOf("Dimensions: 12x8x4 inches", "Material: Mesh, Synthetic"),
            "The Nike Air Max 270 features a sleek design with a large Air unit in the heel for maximum cushioning. The upper is made from a combination of mesh and synthetic materials, providing, breathability..",
            false
        )
    )
    )

    val itemsList: StateFlow<List<ItemsData>> = _itemsList

    fun changeFavorite(index: Int, newFavoriteState : Boolean) {
        _itemsList.update { list ->
            list.mapIndexed { i, item ->
                if (i == index) item.copy(favorite = newFavoriteState) else item
            }
        }
    }

}

