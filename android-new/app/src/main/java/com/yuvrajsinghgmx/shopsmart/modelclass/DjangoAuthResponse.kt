package com.yuvrajsinghgmx.shopsmart.modelclass

import com.google.gson.annotations.SerializedName

data class DjangoAuthResponse(
    val access : String,
    val refresh : String,
    val user: userInfo
)

data class userInfo(
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val name: String,
    val role: String,
    @SerializedName("profile_pic")
    val profilePic: String?,
    @SerializedName("is_new_user")
    val isNewUser: Boolean
)

data class FirebaseIdTokenRequest(
    @SerializedName("id_token") val idToken: String?
)