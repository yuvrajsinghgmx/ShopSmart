package com.yuvrajsinghgmx.shopsmart.modelclass

class Product(
    val productId:Int ,
    val name: String,
    val price: String,
    val category: String,
    val review : String,
    val description: String,
    val imageUrl: List<String>,
    val shopName: String,
    val shopNumber:String,
    val distance: String
)