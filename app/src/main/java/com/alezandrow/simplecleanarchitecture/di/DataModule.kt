package com.alezandrow.simplecleanarchitecture.di

import android.app.AlarmManager
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.room.Room
import com.alezandrow.simplecleanarchitecture.BuildConfig
import com.alezandrow.simplecleanarchitecture.common.DEVICE_IP
import com.alezandrow.simplecleanarchitecture.data.source.local.ReminderDao
import com.alezandrow.simplecleanarchitecture.data.source.local.ReminderDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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
    fun provideReminderDatabase(@ApplicationContext context: Context): ReminderDatabase {
        return Room.databaseBuilder(
            context,
            ReminderDatabase::class.java,
            "reminder_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideReminderDao(database: ReminderDatabase): ReminderDao {
        return database.getReminderDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        val auth = Firebase.auth

        if (BuildConfig.DEBUG) {
            auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            val emulatorHost = DEVICE_IP
            val emulatorPort = 9099

            auth.useEmulator(emulatorHost, emulatorPort)
        }

        return auth
    }

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = Firebase.firestore
        if (BuildConfig.DEBUG) {
            firestore.useEmulator(DEVICE_IP, 8080)
        }
        return firestore
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java) as AlarmManager
    }

}