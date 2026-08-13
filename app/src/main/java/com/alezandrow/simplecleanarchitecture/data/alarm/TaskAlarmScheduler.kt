package com.alezandrow.simplecleanarchitecture.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.alezandrow.simplecleanarchitecture.common.ACTION_TASK_REMINDER
import com.alezandrow.simplecleanarchitecture.common.TASK_ID
import com.alezandrow.simplecleanarchitecture.common.TASK_TITLE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
    private val permissionHelper: AlarmPermissionHelper
) {

    fun schedule(
        taskId: String,
        taskTitle: String,
        taskDueDate: Long,
    ): Boolean {

        if (taskDueDate <= System.currentTimeMillis()) return false

        val pendingIntent = buildPendingIntent(taskId, taskTitle)

        if (permissionHelper.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                taskDueDate,
                pendingIntent
            )
            Log.d("TaskAlarmScheduler", "Success Schedule Alarm")
            return true
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                taskDueDate,
                pendingIntent
            )
            Log.d("TaskAlarmScheduler", "Success Schedule Alarm")
            return true
        }
    }

    fun cancel(taskId: String) {
        Log.d("TaskAlarmScheduler", "task cancel successfully")
        val pendingIntent = buildPendingIntent(taskId, "")
        alarmManager.cancel(pendingIntent)
    }

    private fun buildPendingIntent(taskId: String, taskTitle: String): PendingIntent {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra(TASK_ID, taskId)
            putExtra(TASK_TITLE, taskTitle)
            action = ACTION_TASK_REMINDER
        }
        return PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}