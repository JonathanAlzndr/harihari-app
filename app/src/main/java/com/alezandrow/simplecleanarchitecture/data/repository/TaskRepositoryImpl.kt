package com.alezandrow.simplecleanarchitecture.data.repository

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

    override suspend fun changeTaskStatus(task: Task): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        return try {
            taskDataSource.changeStatus(uid, taskDto)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override suspend fun deleteTask(task: Task): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        return try {
            taskDataSource.deleteTask(uid, taskDto)
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
}