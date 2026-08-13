package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDao {

    @Insert(onConflict = REPLACE)
    suspend fun addNewReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminder WHERE dueDateTime > :now AND status != 'DONE'")
    suspend fun getUpcomingReminders(now: Long = System.currentTimeMillis()): List<ReminderEntity>

    @Query("DELETE FROM reminder WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: String)

}