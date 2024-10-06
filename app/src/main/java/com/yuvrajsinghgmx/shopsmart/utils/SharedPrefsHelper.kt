package com.yuvrajsinghgmx.shopsmart.utils

import android.content.Context
import android.net.Uri

object SharedPrefsHelper {
    private const val PREFS_NAME = "ShopSmartPrefs"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_PROFILE_PHOTO_URI = "profile_photo_uri"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserName(context: Context, name: String) {
        getPrefs(context).edit().putString(KEY_USER_NAME, name).apply()
    }

    fun saveUserEmail(context: Context, email: String) {
        getPrefs(context).edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun saveProfilePhotoUri(context: Context, uri: Uri?) {
        getPrefs(context).edit().putString(KEY_PROFILE_PHOTO_URI, uri?.toString()).apply()
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserEmail(context: Context): String {
        return getPrefs(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getProfilePhotoUri(context: Context): Uri? {
        val uriString = getPrefs(context).getString(KEY_PROFILE_PHOTO_URI, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }
}