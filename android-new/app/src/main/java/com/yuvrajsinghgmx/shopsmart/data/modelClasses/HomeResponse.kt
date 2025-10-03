package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class HomeResponse (
    @SerializedName("message") val message: String,
    @SerializedName("user_location") val userLocation: UserLocation,
    @SerializedName("trending_products") val trendingProducts: List<BaseProduct>,
    @SerializedName("categorized_products") val categorizedProducts: List<CategorizedProduct>,
    @SerializedName("nearby_shops") val nearbyShops: List<NearbyShop>
)

data class UserLocation(
    val additionalProp1: Double?,
    val additionalProp2: Double?,
    val additionalProp3: Double?
)

data class CategorizedProduct(
    val type: String,
    val items: List<BaseProduct>
)

data class NearbyShop(
    val type: String,
    val items: List<BaseShop>
)