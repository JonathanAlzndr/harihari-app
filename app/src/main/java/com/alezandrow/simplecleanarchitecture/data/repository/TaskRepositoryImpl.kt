package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.mapper.toDto
import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDomain
import com.alezandrow.simplecleanarchitecture.data.source.network.SessionDataSource
import com.alezandrow.simplecleanarchitecture.data.source.network.TaskFirestoreDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDataSource: TaskFirestoreDataSource,
    private val sessionDataSource: SessionDataSource
) : TaskRepository {

    override suspend fun addNewTask(task: Task) {
        val uid = sessionDataSource.requireCurrentUid()
        taskDataSource.addNewTask(uid, task.toDto())
    }

    override suspend fun changeTaskStatus(task: Task) {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        taskDataSource.changeStatus(uid, taskDto)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTasks(): Flow<List<Task>> {
        return sessionDataSource.observeCurrentUser()
            .flatMapLatest { user ->
                if (user == null) flowOf(emptyList())
                else taskDataSource.getAllTask(user.uid)
            }
            .map { dto ->
                dto.map { it.toTaskDomain() }
            }
            .distinctUntilChanged()
    }

    override suspend fun deleteTask(task: Task) {
        val uid = sessionDataSource.requireCurrentUid()
        val taskDto = task.toDto()
        taskDataSource.deleteTask(uid, taskDto)
    }

    override fun getTasksByPriority(priority: TaskPriority): Flow<List<Task>> {
        val uid = sessionDataSource.requireCurrentUid()
        return taskDataSource.getTaskByPriority(
            uid,
            priority.name
        ).map { dto ->
            dto.map { it.toTaskDomain() }
        }
    }
}