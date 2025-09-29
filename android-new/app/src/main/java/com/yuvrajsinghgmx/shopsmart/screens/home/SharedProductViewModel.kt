package com.yuvrajsinghgmx.shopsmart.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedProductViewModel @Inject constructor(
//    private val productRepository: ProductRepository
) : ViewModel() {

//    private val _productDetails = MutableLiveData<ProductDetailResponse?>()
//    val productDetails: LiveData<ProductDetailResponse?> = _productDetails

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    fun selectedProduct(product: Product) {
        _selectedProduct.value = product
    }

//    fun fetchProductDetails(productId: String) {
//        viewModelScope.launch {
//            _loading.value = true
//            _error.value = null
//
//            val result = productRepository.getProductDetails(productId)
//
//            if (result != null) {
//                _productDetails.value = result
//            } else {
//                _error.value = "Failed to load product details"
//            }
//
//            _loading.value = false
//        }
//    }
}