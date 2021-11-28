package com.jsloane.littleone.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.data.remote.LittleOneApi
import com.jsloane.littleone.data.repository.AppSettingsRepositoryImpl
import com.jsloane.littleone.data.repository.LittleOneRepositoryImpl
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.LittleOneRepository
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
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAppSettingsRepository(
        @ApplicationContext appContext: Context
    ): AppSettingsRepository = AppSettingsRepositoryImpl(appContext)

    @Provides
    @Singleton
    fun provideLittleOneRepository(api: LittleOneApi): LittleOneRepository =
        LittleOneRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideLittleOneApi(): LittleOneApi = LittleOneApi()
}
