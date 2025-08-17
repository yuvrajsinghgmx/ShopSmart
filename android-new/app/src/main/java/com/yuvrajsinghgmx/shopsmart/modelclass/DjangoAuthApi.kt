package com.yuvrajsinghgmx.shopsmart.modelclass

import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.http.Body
import retrofit2.http.POST

interface DjangoAuthApi{
    @POST("api/auth/firebase/")
    suspend fun exchangeFirebaseToken(@Body body: FirebaseIdTokenRequest): DjangoAuthResponse
}