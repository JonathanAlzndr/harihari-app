package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun addNewTask(task: Task): AppResult<Unit>

    suspend fun updateTask(task: Task): AppResult<Unit>

    suspend fun deleteTask(taskId: String): AppResult<Unit>

    fun getTasksByTitleAndPriority(title: String, priority: TaskPriority?): Flow<AppResult<List<Task>>>

    suspend fun getTaskById(taskId: String): AppResult<Task>
}