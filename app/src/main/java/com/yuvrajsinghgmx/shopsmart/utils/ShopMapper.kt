package com.yuvrajsinghgmx.shopsmart.utils

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.BaseShop
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Shop

fun BaseShop.toUiShop(): Shop {
    return Shop(
        id = id,
        shopId = shopId,
        name = name,
        images = images,
        category = category,
        distance = distance,
        isFavorite = isFavorite,
        averageRating = averageRating,
        description = description,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone
    )
}

