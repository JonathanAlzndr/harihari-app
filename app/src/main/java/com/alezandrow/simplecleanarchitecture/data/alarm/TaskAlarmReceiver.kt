package com.alezandrow.simplecleanarchitecture.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alezandrow.simplecleanarchitecture.common.ACTION_TASK_REMINDER
import com.alezandrow.simplecleanarchitecture.common.TASK_ID
import com.alezandrow.simplecleanarchitecture.common.TASK_TITLE
import com.alezandrow.simplecleanarchitecture.data.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TASK_REMINDER) {
            val taskId = intent.getStringExtra(TASK_ID) ?: ""
            val taskTitle = intent.getStringExtra(TASK_TITLE) ?: ""

            notificationHelper.showReminder(
                taskTitle = taskTitle,
                taskId = taskId
            )
        }
    }
}