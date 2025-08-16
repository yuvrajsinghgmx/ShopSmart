package com.yuvrajsinghgmx.shopsmart.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yuvrajsinghgmx.shopsmart.modelclass.DjangoAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://shopsmart-mt0p.onrender.com/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideDjangoAuthApi(retrofit: Retrofit): DjangoAuthApi =
        retrofit.create(DjangoAuthApi::class.java)

//    @Provides
//    @Singleton
//    fun providetestRetrofit(gson: Gson): Retrofit {
//        Log.d("NetworkModule", "Retrofit instance created")
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//    }
}
