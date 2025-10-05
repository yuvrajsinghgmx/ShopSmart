package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class ProductDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("product_type") val productType: String,
    @SerializedName("stock_quantity") val stockQuantity: Int,
    @SerializedName("images") val images: List<String>,
    @SerializedName("position") val position: Int,
    @SerializedName("shop_name") val shopName: String,
    @SerializedName("shop_id") val shopId: String,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("reviews_count") val reviewsCount: Int,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("shop_details") val shopDetails: Map<String, String>,
    @SerializedName("recent_reviews") val recentReviews: List<Review>
)
