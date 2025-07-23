package com.yuvrajsinghgmx.shopsmart.profilefeatures

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yuvrajsinghgmx.shopsmart.profilefeatures.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {
    private lateinit var languageManager: LanguageManager

    override fun attachBaseContext(newBase: Context) {
        languageManager = LanguageManager(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageManager.applyLanguage(this)
    }
}