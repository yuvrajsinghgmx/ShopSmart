package com.yuvrajsinghgmx.shopsmart.di

import com.google.firebase.auth.FirebaseAuth
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.AuthRepository
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.AuthRepositoryImpl
import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service.AuthService
import com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen.service.AuthServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

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
}
