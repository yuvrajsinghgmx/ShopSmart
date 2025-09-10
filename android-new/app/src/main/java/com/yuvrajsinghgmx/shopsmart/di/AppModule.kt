package com.yuvrajsinghgmx.shopsmart.di

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.yuvrajsinghgmx.shopsmart.data.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.data.repository.AuthRepositoryImpl
import com.yuvrajsinghgmx.shopsmart.data.repository.Repository
import com.yuvrajsinghgmx.shopsmart.screens.auth.service.AuthService
import com.yuvrajsinghgmx.shopsmart.screens.auth.service.AuthServiceImpl
import com.yuvrajsinghgmx.shopsmart.sharedprefs.AuthPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return Repository()
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth(app: Application): FirebaseAuth {
        if (FirebaseApp.getApps(app).isEmpty()) {
            FirebaseApp.initializeApp(app)
        }
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService {
        return authServiceImpl
    }

    // Provides the implementation for the AuthRepository interface.
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideAuthPrefs(@ApplicationContext context: Context): AuthPrefs {
        return AuthPrefs(context)
    }
}
