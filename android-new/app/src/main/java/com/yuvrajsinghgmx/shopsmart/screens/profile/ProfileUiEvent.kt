package com.yuvrajsinghgmx.shopsmart.screens.profile

sealed class ProfileUiEvent{
    data class ShowSnackbar(val message: String): ProfileUiEvent()
    object NavigateBack: ProfileUiEvent()
}
