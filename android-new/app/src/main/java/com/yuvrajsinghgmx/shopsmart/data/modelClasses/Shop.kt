package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class Shop(
    val id: Int,
    val shopId: String,
    val name: String,
    val images: List<String>,
    val category: String,
    val distance: Double,
    val isFavorite: Boolean,
    val averageRating: Double,
    val description: String,
    val address: String
)

data class ShopResponse(
    val id: Int,
    @SerializedName("shop_id") val shopId: String,
    val name: String,
    val images: List<String>,
    val address: String,
    val category: String,
    val description: String,
    @SerializedName("is_approved") val isApproved: Boolean,
    @SerializedName("owner_name") val ownerName: String,
    val distance: Double?,
    @SerializedName("shop_type") val shopType: String,
    val position: Int,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("reviews_count") val reviewsCount: Int,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("created_at") val createdAt: String,
    val sponsored: String,
    @SerializedName("products_count") val productsCount: Int? = null,
    @SerializedName("recent_reviews") val recentReviews: List<Review>? = null,
    @SerializedName("document_images") val documentImages: List<String>? = emptyList(),
    val phone: String,
    @SerializedName("featured_products") val featuredProducts: List<Product>? = emptyList(),
    @SerializedName("all_products") val allProducts: List<Product>? = emptyList()
)
