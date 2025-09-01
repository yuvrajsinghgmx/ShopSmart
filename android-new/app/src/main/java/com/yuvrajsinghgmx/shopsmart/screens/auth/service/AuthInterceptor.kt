package com.yuvrajsinghgmx.shopsmart.screens.auth.service

import android.util.Log
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val authPrefs: AuthPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authPrefs.getAccessToken()
        Log.v("token", " $token")
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}