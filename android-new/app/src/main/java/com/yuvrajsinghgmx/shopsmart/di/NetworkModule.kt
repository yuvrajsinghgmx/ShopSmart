package com.yuvrajsinghgmx.shopsmart.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yuvrajsinghgmx.shopsmart.data.TokenAuthenticator
import com.yuvrajsinghgmx.shopsmart.data.interfaces.DjangoAuthApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.FavoritesApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.OnboardingAPI
import com.yuvrajsinghgmx.shopsmart.data.interfaces.RefreshApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.ReviewApi
import com.yuvrajsinghgmx.shopsmart.data.interfaces.ShopApi
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://shopsmart.slotinsolutions.com/api/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authPrefs: AuthPrefs,
        refreshApi: RefreshApi
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                authPrefs.getAccessToken()?.let { token ->
                    Log.d("AuthDebug", "Sending token: Bearer $token")
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .authenticator(TokenAuthenticator(authPrefs,refreshApi))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideOnboardingApi(retrofit: Retrofit): OnboardingAPI =
        retrofit.create(OnboardingAPI::class.java)

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)

    @Provides
    @Singleton
    fun provideShopApi(retrofit: Retrofit): ShopApi =
        retrofit.create(ShopApi::class.java)

    @Provides
    @Singleton
    fun provideFavoritesApi(retrofit: Retrofit): FavoritesApi =
        retrofit.create(FavoritesApi::class.java)

    @Provides
    @Singleton
    fun provideDjangoAuthApi(retrofit: Retrofit): DjangoAuthApi =
        retrofit.create(DjangoAuthApi::class.java)
}