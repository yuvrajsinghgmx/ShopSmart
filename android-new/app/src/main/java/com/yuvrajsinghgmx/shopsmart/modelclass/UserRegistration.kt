package com.yuvrajsinghgmx.shopsmart.modelclass

import com.yuvrajsinghgmx.shopsmart.screens.UserRole

data class UserRegistration(
    val fullName: String,
    val email: String?,
    val role: UserRole,
    val profileImageUri: String? // store URI as string
)
