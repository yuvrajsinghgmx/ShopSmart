package com.yuvrajsinghgmx.shopsmart.screens.search

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchItem
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchProduct

fun SearchItem.toProduct(): SearchProduct {
    return SearchProduct(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        images = this.images,
        averageRating = this.averageRating,
        reviewsCount = this.reviewsCount,
        shopName = this.shopName,
        distance = this.distance
    )
}