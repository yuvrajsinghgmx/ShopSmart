package com.yuvrajsinghgmx.shopsmart.screens.shops

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.AddShopResponse
import com.yuvrajsinghgmx.shopsmart.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State
import com.google.android.gms.maps.model.LatLng
import com.yuvrajsinghgmx.shopsmart.utils.toRequestBodyJson
import com.yuvrajsinghgmx.shopsmart.utils.toRequestBodyText
import com.yuvrajsinghgmx.shopsmart.utils.uriToMultipart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(private val repository: ShopRepository) : ViewModel() {

    private val _shopResponse = MutableStateFlow<AddShopResponse?>(null)
    val shopResponse: StateFlow<AddShopResponse?> = _shopResponse

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _state = mutableStateOf(ShopFormState())
    val state: State<ShopFormState> = _state

    fun onNameChanged(name: String) {
        _state.value = state.value.copy(name = name)
    }

    fun onCategorySelected(category: String) {
        _state.value = state.value.copy(category = category)
    }

    fun onPhoneChanged(phone: String) {
        val filtered = phone.filter { it.isDigit() }.take(10)
        _state.value = state.value.copy(
            phoneNumber = filtered,
            isPhoneError = filtered.isNotEmpty() && filtered.length != 10
        )
    }

    fun onDescriptionChanged(description: String) {
        _state.value = state.value.copy(
            description = description,
            isDescriptionError = description.length > 1000
        )
    }

    fun onImagePicked(uri: Uri) {
        _state.value = state.value.copy(imageUris = _state.value.imageUris + uri)
    }

    fun onDocumentPicked(uri: Uri) {
        _state.value = _state.value.copy(documentUris = _state.value.documentUris + uri)
    }

    fun removeImage(uri: Uri) {
        _state.value = _state.value.copy(imageUris = _state.value.imageUris - uri)
    }

    fun removeDocument(uri: Uri) {
        _state.value = _state.value.copy(documentUris = _state.value.documentUris - uri)
    }

    fun startLocationPicking() {
        _state.value = state.value.copy(isPickingLocation = true)
    }

    fun onLocationPicked(location: LatLng, address: String) {
        _state.value = state.value.copy(
            location = location,
            shopAddress = address,
            isPickingLocation = false
        )
    }
    fun cancelLocationPicking() {
        _state.value = state.value.copy(isPickingLocation = false)
    }
    fun isFormValid(): Boolean {
        val s = state.value
        return s.name.isNotBlank() &&
                s.category != "Select category" &&
                s.phoneNumber.length == 10 &&
                s.shopAddress.isNotBlank() &&
                !s.isPhoneError &&
                !s.isDescriptionError
    }

    fun saveShop(context: Context) {
        val s = state.value
        viewModelScope.launch {
            try {
                _loading.value = true

                // Multiple extra images
                val extraImages = s.imageUris.mapNotNull { uri ->
                    uriToMultipart(context, uri, "image_uploads")
                }

                // Multiple documents
                val documents = s.documentUris.mapNotNull { uri ->
                    uriToMultipart(context, uri, "document_uploads")
                }

                val imageParts = extraImages.map { it.part }
                val documentParts = documents.map { it.part }

                // Convert text fields
                val name = s.name.toRequestBodyText()
                val address = s.shopAddress.toRequestBodyText()
                val category = s.category.toRequestBodyText()
                val description = s.description.toRequestBodyText()
                val shopType = s.category.toRequestBodyText()
                val position = "3".toRequestBodyText()
                val latitude = (s.location?.latitude ?: 0.0).toString().toRequestBodyText()
                val longitude = (s.location?.longitude ?: 0.0).toString().toRequestBodyText()

                // API call
                val addShopResponse = repository.addShop(
                    name, address, category, description, shopType, position,
                    latitude, longitude, imageParts, documentParts
                )

                if (addShopResponse.isSuccessful) {
                    addShopResponse.body()?.let { body ->
                        _shopResponse.value = body
                        Toast.makeText(context, "Shop added successfully", Toast.LENGTH_SHORT).show()
                        resetForm()
                        Log.d("UploadCheck", "Images uploaded: ${body.images}")
                    } ?: run {
                        _error.value = "Empty response body"
                    }
                } else {
                    val errBody = addShopResponse.errorBody()?.string()
                    _error.value = "Error ${addShopResponse.code()}: $errBody"
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun resetForm() {
        _state.value = ShopFormState()
    }
}