package com.yuvrajsinghgmx.shopsmart.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPrefs @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        userId: Int?,
        name: String?,
        phone: String?,
        profilePic: String?,
        isNewUser: Boolean,
        role: String?
    ) {
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            userId?.let { putInt("user_id", it) }
            putString("name", name)
            putString("phone", phone)
            putString("profile_pic", profilePic)
            putBoolean("is_new_user",isNewUser )
            putString("role", role)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getName(): String? = prefs.getString("name", null)
    fun getPhone(): String? = prefs.getString("phone", null)
    fun getProfilePic(): String? = prefs.getString("profile_pic", null)
    fun getRole(): String? = prefs.getString("role", null)
    fun isNewUser(): Boolean = prefs.getBoolean("is_new_user", false)
    fun getUser(): User? {
        val id = getUserId()
        val name = getName()
        val phone = getPhone()
        val profilePic = getProfilePic()
        val role = getRole()

        return if (id!=-1 && name!=null && role!=null){
            User(
                userId = id,
                userName = name,
                userPhoneNumber = phone,
                profilePic = profilePic,
                userType = role
            )
        }else{
            null
        }
    }

    fun clearAuthData() {
        prefs.edit { clear() }
    }
}
