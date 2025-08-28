package com.yuvrajsinghgmx.shopsmart.screens.home

sealed class HomeEvent {
    object LoadProducts : HomeEvent()
    data class Search(val query: String) : HomeEvent()
    object LoadShops : HomeEvent()
    data class SelectCategory(val category: String) : HomeEvent()
}
