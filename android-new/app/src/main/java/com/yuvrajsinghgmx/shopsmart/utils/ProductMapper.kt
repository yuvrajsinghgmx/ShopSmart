package com.yuvrajsinghgmx.shopsmart.utils

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.BaseProduct
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product

fun BaseProduct.toUiProduct(): Product {
    return Product(
        id = id,
        productId = productId,
        name = name,
        price = price,
        category = category,
        review = "",
        description = description,
        imageUrl = images,
        shopName = shopName,
        shopId = shopId,
        shopNumber = "",
        distance = "",
        isFavorite = isFavorite
    )
}