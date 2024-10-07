package com.yuvrajsinghgmx.shopsmart.utils

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuvrajsinghgmx.shopsmart.datastore.Poduct

object SharedPrefsHelper {
    private const val PREFS_NAME = "ShopSmartPrefs"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_PROFILE_PHOTO_URI = "profile_photo_uri"
    private const val KEY_ORDERS = "orders"

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

    // New methods for handling orders
    fun saveOrders(context: Context, orders: List<Poduct>) {
        val gson = Gson()
        val json = gson.toJson(orders)
        getPrefs(context).edit().putString(KEY_ORDERS, json).apply()
    }

    fun getOrders(context: Context): List<Poduct> {
        val gson = Gson()
        val json = getPrefs(context).getString(KEY_ORDERS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Poduct>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}