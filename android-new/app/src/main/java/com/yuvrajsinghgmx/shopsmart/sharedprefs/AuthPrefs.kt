package com.yuvrajsinghgmx.shopsmart.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AuthPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        name: String,
        phone: String,
        profilePic: String?
    ) {
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putInt("user_id", userId)
            putString("name", name)
            putString("phone", phone)
            putString("profile_pic", profilePic)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getName(): String? = prefs.getString("name", null)
    fun getPhone(): String? = prefs.getString("phone", null)
    fun getProfilePic(): String? = prefs.getString("profile_pic", null)

    fun clearAuthData() {
        prefs.edit { clear() }
    }
}
