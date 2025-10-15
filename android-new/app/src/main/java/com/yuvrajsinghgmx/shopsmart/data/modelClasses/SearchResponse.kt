package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName


data class SearchItem(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("shop_name")
    val shopName: String,

    @SerializedName("distance")
    val distance: Double,

    @SerializedName("average_rating")
    val averageRating: Double,

    @SerializedName("reviews_count")
    val reviewsCount: Int
)

data class SearchProduct (
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val images: List<String>,
    val shopName: String,
    val distance : Double,
    val averageRating: Double,
    val reviewsCount: Int
)