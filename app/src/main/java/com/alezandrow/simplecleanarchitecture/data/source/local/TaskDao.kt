package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    suspend fun addNewTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Update
    suspend fun changeTaskStatus(task: Task)

}