package com.yuvrajsinghgmx.shopsmart.profilefeatures

import android.app.Activity
import android.content.Context
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "language_prefs"
        private const val SELECTED_LANGUAGE = "selected_language"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLanguage(languageCode: String) {
        prefs.edit().putString(SELECTED_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(SELECTED_LANGUAGE, Locale.getDefault().language) ?: "en"
    }

    fun applyLanguage(activity: Activity) {
        val locale = Locale(getLanguage())
        val config = activity.resources.configuration
        config.setLocale(locale)
        activity.createConfigurationContext(config)
    }
}