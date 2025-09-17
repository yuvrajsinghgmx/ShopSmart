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
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ShopRequest
import com.yuvrajsinghgmx.shopsmart.utils.uriToMultipart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    init {
        fetchShops()
    }

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

    fun onProfileImagePicked(uri: Uri?) {
        _state.value = state.value.copy(profileImageUri = uri)
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

/*    fun saveShop(context: Context) {
        val s = state.value
        viewModelScope.launch {
            try {
                _loading.value = true

                val categoryBody = s.category.toRequestBody("text/plain".toMediaTypeOrNull())

//                val imageParts = s.imageUri?.let { listOf(uriToMultipart(context, it, "image_uploads")) } ?: emptyList()
//                val documentParts = emptyList<MultipartBody.Part>()

                val profileImagePart = s.profileImageUri?.let {
                    uriToMultipart(context, it, "images") // field name must match API
                }


                val imageParts = s.imageUris.mapIndexed { index, uri ->
                    uriToMultipart(context, uri, "image_uploads[$index]")
                }

                val documentParts = s.documentUris.mapIndexed { index, uri ->
                    uriToMultipart(context, uri, "document_uploads[$index]")
                }

                val result = repository.addShop(
                    name = s.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    category = categoryBody,
                    address = s.shopAddress.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description = s.description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    latitude = s.location?.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    longitude = s.location?.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    shopType = categoryBody,
                    position = "3".toRequestBody("text/plain".toMediaTypeOrNull()),
                    images = listOfNotNull(profileImagePart) + imageParts,
                    documents = documentParts
                )

                result.onSuccess {
                    _shopResponse.value = it
                    Toast.makeText(context, "Shop added successfully", Toast.LENGTH_SHORT).show()
                    resetForm()
                }.onFailure {
                    _error.value = it.message
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }*/

    fun saveShop(context: Context) {
        val s = state.value
        viewModelScope.launch {
            try {
                _loading.value = true

                // Convert picked URIs -> String file paths (or upload them first, then use URLs if backend requires URLs)
                val imageStrings = s.imageUris.map { it.toString() }
                val documentStrings = s.documentUris.map { it.toString() }

                val shopRequest = ShopRequest(
                    name = s.name,
                    images = s.profileImageUri?.toString() ?: "", // single string
                    address = s.shopAddress,
                    category = s.category,
                    description = s.description,
                    shop_type = s.category, // adjust if shopType differs
                    position = 3, // or dynamic
                    image_uploads = imageStrings,
                    document_uploads = documentStrings,
                    latitude = s.location?.latitude ?: 0.0,
                    longitude = s.location?.longitude ?: 0.0
                )

                val result = repository.addShop(shopRequest)

                result.onSuccess {
                    _shopResponse.value = it
                    Toast.makeText(context, "Shop added successfully", Toast.LENGTH_SHORT).show()
                    resetForm()
                }.onFailure {
                    _error.value = it.message
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

    fun fetchShops() {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(isLoadingShops = true)
                val result = repository.getShops()
                result.onSuccess { shops ->
                    _state.value = state.value.copy(shops = shops, isLoadingShops = false)
                    shops.forEach { shop ->
                        Log.d("ShopViewModel", "Shop: ${shop.name}, Owner: ${shop.owner_name}, Address: ${shop.address}")
                    }
                }.onFailure { error ->
                    _state.value = state.value.copy(errorShops = error.message, isLoadingShops = false)
                }
            }catch (e: Exception) {
                _state.value = state.value.copy(
                    errorShops = e.message ?: "Unexpected error",
                    isLoadingShops = false
                )
                Log.e("ShopViewModel", "Exception in fetchShops", e)
            }finally {
                _state.value = state.value.copy(isLoadingShops = false)
            }

        }
    }

}
