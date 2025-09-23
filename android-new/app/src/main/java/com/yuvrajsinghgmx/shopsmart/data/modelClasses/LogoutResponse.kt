package com.yuvrajsinghgmx.shopsmart.data.modelClasses

data class LogoutRequest(
    val refresh: String
)

data class LogoutResponse(
    val message: String
)