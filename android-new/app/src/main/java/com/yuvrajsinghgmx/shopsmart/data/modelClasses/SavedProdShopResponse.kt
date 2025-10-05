package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class SavedProductResponse(
    val id: Int,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: Double,
    @SerializedName("product_images")
    val productImage: List<String?>,
    @SerializedName("product_category")
    val productCategory: String,
    @SerializedName("shop_name")
    val shopName: String,
    @SerializedName("shop_id")
    val shopId: String,
    @SerializedName("average_rating")
    val averageRating: Float,
    @SerializedName("added_at")
    val addedAt: String
)

data class SavedShopResponse(
    val id: Int,
    @SerializedName("shop_id")
    val shopId: String,
    @SerializedName("shop_name")
    val shopName: String,
    @SerializedName("shop_images")
    val shopImages: List<String?>,
    @SerializedName("shop_category")
    val shopCategory: String,
    @SerializedName("shop_address")
    val shopAddress: String,
    @SerializedName("average_rating")
    val averageRating: Float,
    @SerializedName("added_at")
    val addedAt: String
)

data class ToggleFavoriteProductResponse(
    val message: String,
    @SerializedName("is_favorite")
    val isFavorite: Boolean,
    @SerializedName("product_id")
    val productId: String
)

data class ToggleFavoriteShopResponse(
    val message: String,
    @SerializedName("is_favorite")
    val isFavorite: Boolean,
    @SerializedName("shop_id")
    val shopId: String
)
