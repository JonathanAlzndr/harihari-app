package com.alezandrow.simplecleanarchitecture.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alezandrow.simplecleanarchitecture.data.alarm.TaskAlarmScheduler
import com.alezandrow.simplecleanarchitecture.data.source.local.ReminderDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RescheduleAlarmWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val reminderDao: ReminderDao,
    private val alarmScheduler: TaskAlarmScheduler
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d("Worker", "doWork called")
        return try {
            val upcomingReminders = reminderDao.getUpcomingReminders()
            upcomingReminders.forEach { reminder ->
                val scheduled =
                    alarmScheduler.schedule(reminder.id, reminder.title, reminder.dueDateTime)

                if (!scheduled) {
                    reminderDao.deleteReminderById(reminder.id)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}