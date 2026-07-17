package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun addNewTask(task: Task)

    suspend fun changeTaskStatus(task: Task)

    suspend fun deleteTask(task: Task)

    fun getTaskByTitleAndPriority(title: String, priority: TaskPriority?): Flow<List<Task>>

}