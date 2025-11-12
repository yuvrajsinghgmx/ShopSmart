package com.yuvrajsinghgmx.shopsmart.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yuvrajsinghgmx.shopsmart.data.interfaces.RefreshApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://shopsmart.slotinsolutions.com/api/"

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule{

    @Provides
    @Singleton
    @Named("authGson")
    fun provideAuthGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        @Named("authGson") gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideRefreshApi(@Named("authRetrofit") retrofit: Retrofit): RefreshApi =
        retrofit.create(RefreshApi::class.java)
}