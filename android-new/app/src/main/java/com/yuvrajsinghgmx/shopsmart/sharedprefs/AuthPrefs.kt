package com.yuvrajsinghgmx.shopsmart.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.yuvrajsinghgmx.shopsmart.modelclass.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        name: String,
        phone: String,
        profilePic: String?,
        isNewUser: Boolean
    ) {
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putInt("user_id", userId)
            putString("name", name)
            putString("phone", phone)
            putString("profile_pic", profilePic)
            putBoolean("is_new_user",isNewUser )
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getName(): String? = prefs.getString("name", null)
    fun getPhone(): String? = prefs.getString("phone", null)
    fun getProfilePic(): String? = prefs.getString("profile_pic", null)
    fun isNewUser(): Boolean = prefs.getBoolean("is_new_user", false)

    fun clearAuthData() {
        prefs.edit { clear() }
    }

    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString("user_data", json).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString("user_data", null)
        return if (json != null) {
            gson.fromJson(json, User::class.java)
        } else null
    }

    fun clearUser() {
        prefs.edit().remove("user_data").apply()
    }
}
