package com.alezandrow.simplecleanarchitecture.data.repository

import android.util.Log
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.data.alarm.TaskAlarmScheduler
import com.alezandrow.simplecleanarchitecture.data.mapper.FirestoreErrorMapper
import com.alezandrow.simplecleanarchitecture.data.mapper.toDto
import com.alezandrow.simplecleanarchitecture.data.mapper.toReminderEntity
import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDomain
import com.alezandrow.simplecleanarchitecture.data.source.local.ReminderDao
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
    private val taskNetworkDataSource: TaskFirestoreDataSource,
    private val reminderDao: ReminderDao,
    private val sessionDataSource: SessionDataSource,
    private val taskAlarmScheduler: TaskAlarmScheduler
) : TaskRepository {

    override suspend fun addNewTask(task: Task): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()

        return try {

            val taskId = taskNetworkDataSource.generateTaskId(uid)
            val taskWithId = task.copy(id = taskId)
            val taskDto = taskWithId.toDto()

            taskNetworkDataSource.addNewTask(uid, taskDto)
            if (taskAlarmScheduler.schedule(taskWithId.id, taskWithId.title, taskWithId.dueDate)) {
                val reminder = taskDto.toReminderEntity()
                reminderDao.addNewReminder(reminder)
            }
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
            taskAlarmScheduler.cancel(taskDto.id)
            reminderDao.updateReminder(taskDto.toReminderEntity())
            taskNetworkDataSource.updateTask(uid, taskDto)

            val result = taskAlarmScheduler.schedule(
                taskId = taskDto.id,
                taskTitle = taskDto.title,
                taskDueDate = taskDto.dueDate
            )

            Log.d("Update Task Repo", "updateTask: $result")
            AppResult.Success(Unit)
        } catch (e: Exception) {
            Log.d("Update Task Repo", "updateTask: Failed ${e.message}")
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override suspend fun deleteTask(taskId: String): AppResult<Unit> {
        val uid = sessionDataSource.requireCurrentUid()
        return try {
            taskAlarmScheduler.cancel(taskId)
            reminderDao.deleteReminderById(taskId)
            taskNetworkDataSource.deleteTask(uid, taskId)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirestoreErrorMapper.map(e))
        }
    }

    override fun getTasksByTitleAndPriority(
        title: String,
        priority: TaskPriority?
    ): Flow<AppResult<List<Task>>> {
        Log.d("TaskRepoImpl", "getTaskByTitleAndPriority called")
        val uid = try {
            sessionDataSource.requireCurrentUid()
        } catch (e: Exception) {
            Log.e("TaskRepoImpl", "requireCurrentUid failed", e)
            throw e
        }
        Log.d("TaskRepoImpl", "uid = $uid")
        return taskNetworkDataSource.getTaskByTitleAndPriority(uid, title, priority?.name)
            .map { tasksDto ->
                tasksDto.map { dto ->
                    dto.toTaskDomain()
                }
            }.map {
                AppResult.Success(it)
            }.catch { e ->
                Log.d("TaskRepoImpl", "exception $e")
                AppResult.Error(FirestoreErrorMapper.map(e as Exception))
            }
    }

    override suspend fun getTaskById(taskId: String): AppResult<Task> {
        return try {
            val uid = sessionDataSource.requireCurrentUid()
            val result = taskNetworkDataSource.getTaskById(uid, taskId)
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