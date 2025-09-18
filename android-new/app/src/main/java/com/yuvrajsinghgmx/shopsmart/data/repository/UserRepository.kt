package com.yuvrajsinghgmx.shopsmart.data.repository

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.User
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val authPrefs: AuthPrefs
) {
    fun getLoggedInUser(): User? {
        val userId = authPrefs.getUserId()
        val name = authPrefs.getName()
        val phone = authPrefs.getPhone()
        val profilePic = authPrefs.getProfilePic()
        val role = authPrefs.getRole()

        return if (name !=null){
            User(
                userId = userId,
                userName = name,
                userPhoneNumber = phone,
                profilePic = profilePic,
                userType = role
            )
        } else {
            null
        }
    }
}