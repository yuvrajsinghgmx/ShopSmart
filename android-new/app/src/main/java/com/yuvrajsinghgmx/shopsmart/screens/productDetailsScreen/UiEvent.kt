package com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen

import com.yuvrajsinghgmx.shopsmart.modelclass.Product

sealed class UiEvent {
    data class ShareProduct(val product: Product) : UiEvent()
    data class CallShop(val phoneNumber: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}