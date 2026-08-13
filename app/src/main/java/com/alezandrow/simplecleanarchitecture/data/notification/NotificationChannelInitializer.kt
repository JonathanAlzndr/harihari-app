package com.alezandrow.simplecleanarchitecture.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.alezandrow.simplecleanarchitecture.common.CHANNEL_DESCRIPTION
import com.alezandrow.simplecleanarchitecture.common.CHANNEL_ID
import com.alezandrow.simplecleanarchitecture.common.CHANNEL_NAME

object NotificationChannelInitializer {
    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableVibration(true)
        }

        val notificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}