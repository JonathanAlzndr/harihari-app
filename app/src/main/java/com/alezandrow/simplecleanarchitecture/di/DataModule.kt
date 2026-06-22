package com.alezandrow.simplecleanarchitecture.di

import android.content.Context
import androidx.room.Room
import com.alezandrow.simplecleanarchitecture.BuildConfig
import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDao
import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesTaskDao(database: TaskDatabase): TaskDao {
        return database.getTaskDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        val auth = Firebase.auth

        if (BuildConfig.DEBUG) {
            auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            val emulatorHost = "192.168.1.5"
            val emulatorPort = 9099

            auth.useEmulator(emulatorHost, emulatorPort)
        }

        return auth
    }

}