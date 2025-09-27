package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class SearchProductResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val images: String,
    val shop_name: String,
    val distance: Double,
    val average_rating: Double,
    val reviews_count: Int
)

fun SearchProductResponse.toSearchResult(): SearchResult {
    val product = Product(
        productId = id.toString(),
        name = name,
        price = price,
        category = "", // API doesn’t give category, so keep blank or default
        review = average_rating.toString(),
        description = description,
        imageUrl = listOf(images),
        shopName = shop_name,
        shopId = "", // not in API
        shopNumber = "",
        distance = distance.toString(),
        isFavorite = false
    )

    val shop = Shop(
        shopId = "", // not in API
        shopName = shop_name,
        shopNumber = "",
        distance = distance.toString(),
        imageUrl = listOf(images),
        category = "",
        latitude = 0.0,
        longitude = 0.0,
        isFavorite = false
    )

    return SearchResult(product = product, shop = shop)
}


