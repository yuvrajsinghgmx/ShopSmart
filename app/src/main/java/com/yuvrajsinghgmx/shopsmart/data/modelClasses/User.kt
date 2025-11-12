package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class User(
    val userId: Int,
    val userName: String,
    val userPhoneNumber: String?,
    val userType: String?,
    val savedProducts: List<Product> = emptyList(),
    val savedShops: List<Shop> = emptyList(),
    val profilePic: String?
)