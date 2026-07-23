package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun addNewTask(task: Task): AppResult<Unit>

    suspend fun changeTaskStatus(task: Task): AppResult<Unit>

    suspend fun deleteTask(task: Task): AppResult<Unit>

    fun getTaskByTitleAndPriority(title: String, priority: TaskPriority?): Flow<AppResult<List<Task>>>

}