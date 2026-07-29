package com.alezandrow.simplecleanarchitecture.data.repository

import android.util.Log
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.data.mapper.FirestoreErrorMapper
import com.alezandrow.simplecleanarchitecture.data.mapper.toDto
import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDomain
import com.alezandrow.simplecleanarchitecture.data.source.network.SessionDataSource
import com.alezandrow.simplecleanarchitecture.data.source.network.TaskFirestoreDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDataSource: TaskFirestoreDataSource,
    private val sessionDataSource: SessionDataSource
) : TaskRepository {

    override suspend fun addNewTask(task: Task): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        return try {
            taskDataSource.addNewTask(uid, taskDto)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override suspend fun updateTask(task: Task): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        Log.d("Update Task Repo", "updateTask -> taskDtoId : ${taskDto.id} taskId: ${task.id} ")
        return try {
            taskDataSource.updateTask(uid, taskDto)
            Log.d("Update Task Repo", "updateTask: Success")
            AppResult.Success(Unit)
        } catch (e: Exception) {
            Log.d("Update Task Repo", "updateTask: Failed ${e.message}")
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override suspend fun deleteTask(taskId: String): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        return try {
            taskDataSource.deleteTask(uid, taskId)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override fun getTaskByTitleAndPriority(
        title: String,
        priority: TaskPriority?
    ): Flow<AppResult<List<Task>>> {
        val uid = sessionDataSource.requireCurrentUid()
        return taskDataSource.getTaskByTitleAndPriority(uid, title, priority?.name)
            .map { tasksDto ->
                tasksDto.map { dto ->
                    dto.toTaskDomain()
                }
            }.map {
                AppResult.Success(it)
            }.catch { e ->
                AppResult.Error(FirestoreErrorMapper.map(e as Exception))
            }
    }

    override suspend fun getTaskById(taskId: String): AppResult<Task> {
        return try {
            val uid = sessionDataSource.requireCurrentUid()
            val result = taskDataSource.getTaskById(uid, taskId)
            if (result != null) {
                AppResult.Success(result.toTaskDomain())
            } else {
                AppResult.Error(AppError.NotFound)
            }
        } catch (e: Exception) {
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }
}