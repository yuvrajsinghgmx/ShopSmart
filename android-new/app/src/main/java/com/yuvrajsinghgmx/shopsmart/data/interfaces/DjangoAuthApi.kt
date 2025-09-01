package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.DjangoAuthResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.FirebaseIdTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DjangoAuthApi{
    @POST("api/auth/firebase/")
    suspend fun exchangeFirebaseToken(@Body body: FirebaseIdTokenRequest): DjangoAuthResponse
}