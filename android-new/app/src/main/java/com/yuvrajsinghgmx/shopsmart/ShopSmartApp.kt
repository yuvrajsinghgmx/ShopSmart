package com.yuvrajsinghgmx.shopsmart

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShopSmartApp : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (!Places.isInitialized()) {
            Places.initialize(this, "")
        }
    }
}