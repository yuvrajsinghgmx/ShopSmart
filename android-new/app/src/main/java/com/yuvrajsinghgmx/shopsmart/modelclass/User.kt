package com.yuvrajsinghgmx.shopsmart.modelclass

data class User(
    val userId: String,
    val userName: String,
    val userPhoneNumber: Int,
    val userType: String,
    val savedProducts: List<Product>,
    val savedShops: List<Shop>
)
