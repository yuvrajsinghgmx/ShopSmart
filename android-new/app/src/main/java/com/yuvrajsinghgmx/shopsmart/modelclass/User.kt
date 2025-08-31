package com.yuvrajsinghgmx.shopsmart.modelclass

import com.google.gson.annotations.SerializedName

/*data class User(
    val userId: String,
    val userName: String,
    val userPhoneNumber: Int,
    val userType: String,
    val savedProducts: List<Product>,
    val savedShops: List<Shop>
)*/

/*data class User(
    @SerializedName("id")
    val userId: String,

    @SerializedName("username")
    val userName: String,

    @SerializedName("phone_number")
    val userPhoneNumber: Int,

    @SerializedName("role")
    val userType: String,

    @SerializedName("profile_image") // from registration payload
    val profileImage: String? = null,

    @SerializedName("current_address")
    val currentAddress: String? = null,

    @SerializedName("is_new_user")
    val isNewUser: Boolean? = null,

    @SerializedName("favorite_products")
    val savedProducts: List<Product>,

    @SerializedName("favorite_shops")
    val savedShops: List<Shop>
)*/

data class User(
    @SerializedName("id")
    val userId: Int = 0,  // JSON has integer id

    @SerializedName("phone_number")
    val userPhoneNumber: String = "",  // JSON has phone number as string

    @SerializedName("name")
    val userName: String = "",  // optional, can be null

    @SerializedName("role")
    val userType: String = "CUSTOMER",  // default role

    @SerializedName("profile_pic") // from login response
    val profilePic: String? = null,

    @SerializedName("is_new_user")
    val isNewUser: Boolean = true,  // default to new user

    @SerializedName("favorite_shops")
    val savedShops: List<Shop> = emptyList(),

    @SerializedName("favorite_products")
    val savedProducts: List<Product> = emptyList(),


)


