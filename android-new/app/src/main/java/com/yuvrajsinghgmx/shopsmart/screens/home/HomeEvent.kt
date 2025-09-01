package com.yuvrajsinghgmx.shopsmart.screens.home

sealed class HomeEvent {
    object LoadProducts : HomeEvent()
    data class Search(val query: String) : HomeEvent()
    object LoadShops : HomeEvent()
}