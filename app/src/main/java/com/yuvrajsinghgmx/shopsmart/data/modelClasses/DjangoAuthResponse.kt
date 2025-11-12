package com.yuvrajsinghgmx.shopsmart.data.modelClasses

import com.google.gson.annotations.SerializedName

data class DjangoAuthResponse(
    val access : String,
    val refresh : String,
    val user: UserInfo
)

data class UserInfo(
    val id: Int?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    val name: String?,
    val role: String?,
    @SerializedName("profile_pic")
    val profilePic: String?,
    @SerializedName("is_new_user")
    val isNewUser: Boolean,
    @SerializedName("favorite_shops")
    val favoriteShops: List<Shop>?,
    @SerializedName("favorite_products")
    val favoriteProducts: List<Product>?
)

data class FirebaseIdTokenRequest(
    @SerializedName("id_token") val idToken: String?
)