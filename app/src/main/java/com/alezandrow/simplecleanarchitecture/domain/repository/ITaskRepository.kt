package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {

    suspend fun addNewTask(task: Task)

    suspend fun changeTaskStatus(task: Task)

    fun getAllTasks(): Flow<List<Task>>

    suspend fun deleteTask(task: Task)

}