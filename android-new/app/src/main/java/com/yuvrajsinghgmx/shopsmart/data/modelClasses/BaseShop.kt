package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class BaseShop(
    @SerializedName("id") val id: Int,
    @SerializedName("shop_id") val shopId: String,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("address") val address: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("is_approved") val isApproved: Boolean,
    @SerializedName("owner_name") val ownerName: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("shop_type") val shopType: String,
    @SerializedName("position") val position: Int?,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("reviews_count") val reviewsCount: Int,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("sponsored") val sponsored: Boolean
)