package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ProductDetailResponse

sealed class UiEvent {
    data class ShareProduct(val product: ProductDetailResponse) : UiEvent()
    data class CallShop(val phoneNumber: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}