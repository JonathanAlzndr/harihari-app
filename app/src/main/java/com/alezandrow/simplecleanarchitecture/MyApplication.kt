package com.alezandrow.simplecleanarchitecture

import android.app.Application
import com.alezandrow.simplecleanarchitecture.data.notification.NotificationChannelInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationChannelInitializer.createChannel(this)
    }
}