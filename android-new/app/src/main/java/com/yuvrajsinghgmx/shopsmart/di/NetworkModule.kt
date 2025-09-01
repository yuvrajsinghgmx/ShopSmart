package com.yuvrajsinghgmx.shopsmart.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yuvrajsinghgmx.shopsmart.api.ReviewApi
import com.yuvrajsinghgmx.shopsmart.modelclass.DjangoAuthApi
import com.yuvrajsinghgmx.shopsmart.screens.auth.service.AuthInterceptor
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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

    @Provides
    @Singleton
    fun provideOkhttpClient(authPrefs: AuthPrefs): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authPrefs))
            .build()


    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthenticatedRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideReviewApi(@Named("authRetrofit") retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)
}
