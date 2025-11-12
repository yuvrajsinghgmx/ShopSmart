package com.yuvrajsinghgmx.shopsmart.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchItem
import com.yuvrajsinghgmx.shopsmart.data.repository.SearchRepository
import com.yuvrajsinghgmx.shopsmart.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchState = MutableStateFlow<Resource<List<SearchItem>>>(Resource.Success(emptyList()))
    val searchState = _searchState.asStateFlow()
    private var searchJob: Job? = null
    fun searchProducts(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchState.value = Resource.Success(emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(500L)
            searchRepository.searchProducts(query)
                .onEach { result ->
                    _searchState.value = result
                }
                .launchIn(this)
        }
    }
}