package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    suspend fun addNewTask(task: TaskDbEntity)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<TaskDbEntity>>

    @Update
    suspend fun changeTaskStatus(task: TaskDbEntity)

    @Delete
    suspend fun deleteTask(task: TaskDbEntity)

}