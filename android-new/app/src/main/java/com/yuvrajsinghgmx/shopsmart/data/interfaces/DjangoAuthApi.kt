package com.yuvrajsinghgmx.shopsmart.data.interfaces

import com.yuvrajsinghgmx.shopsmart.data.modelClasses.DjangoAuthResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.FirebaseIdTokenRequest
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshResponse
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DjangoAuthApi{
    @POST("auth/firebase/")
    suspend fun exchangeFirebaseToken(@Body body: FirebaseIdTokenRequest): DjangoAuthResponse
}