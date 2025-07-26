package com.yuvrajsinghgmx.shopsmart.di

import com.yuvrajsinghgmx.shopsmart.modelclass.repository.Repository
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
}