package com.yuvrajsinghgmx.shopsmart.screens.search

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchItem
import com.yuvrajsinghgmx.shopsmart.utils.Resource

data class SearchScreenState(
    val searchQuery: String = "",
    val searchResult: Resource<List<SearchItem>> = Resource.Success(emptyList())
)