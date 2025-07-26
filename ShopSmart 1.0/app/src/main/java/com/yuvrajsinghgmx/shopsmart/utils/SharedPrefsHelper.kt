package com.yuvrajsinghgmx.shopsmart.utils

import android.content.Context
import android.net.Uri
import androidx.webkit.internal.ApiFeature.T
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuvrajsinghgmx.shopsmart.datastore.Product

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

    fun clearUserData(context: Context) {
        val editor = getPrefs(context).edit()
        editor.clear()
        editor.apply()
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

    fun saveOrders(context: Context, orders: List<Product>) {
        val gson = Gson()
        val json = gson.toJson(orders)
        getPrefs(context).edit().putString(KEY_ORDERS, json).apply()
    }

    fun getOrders(context: Context): List<Product> {
        val gson = Gson()
        val json = getPrefs(context).getString(KEY_ORDERS, null)
        return if (json != null) {
            val type = object : TypeToken<Product>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}