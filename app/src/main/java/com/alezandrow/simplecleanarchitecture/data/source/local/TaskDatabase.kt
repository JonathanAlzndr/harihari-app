package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([TaskDbEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

}