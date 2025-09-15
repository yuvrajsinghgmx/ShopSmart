package com.yuvrajsinghgmx.shopsmart.screens.shops

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopItem

data class ShopFormState(
    val name: String = "",
    val category: String = "Select category",
    val phoneNumber: String = "",
    val shopAddress: String = "",
    val description: String = "",
    val profileImageUri: Uri? = null,
    val imageUris: List<Uri> = emptyList(),
    val documentUris: List<Uri> = emptyList(),
    val location: LatLng? = null,
    val isPickingLocation: Boolean = false,
    val isPhoneError: Boolean = false,
    val isDescriptionError: Boolean = false,
    val shops: List<ShopItem> = emptyList(),
    val isLoadingShops: Boolean = false,
    val errorShops: String? = null
)
