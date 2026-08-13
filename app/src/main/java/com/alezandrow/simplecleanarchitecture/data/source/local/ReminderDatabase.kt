package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([ReminderEntity::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao

}