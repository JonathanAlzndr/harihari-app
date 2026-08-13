package com.alezandrow.simplecleanarchitecture.data.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alezandrow.simplecleanarchitecture.MainActivity
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.common.CHANNEL_ID
import com.alezandrow.simplecleanarchitecture.common.TASK_ID
import com.alezandrow.simplecleanarchitecture.common.TASK_TITLE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun showReminder(taskId: String, taskTitle: String) {
        if(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TASK_ID, taskId)
            putExtra(TASK_TITLE, taskTitle)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.hashCode(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(taskTitle)
            .setContentText("The task is due date now")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(taskId.hashCode(), notification)
    }
}