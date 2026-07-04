package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.mapper.toDto
import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDomain
import com.alezandrow.simplecleanarchitecture.data.source.network.TaskFirestoreDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryFirestoreImpl @Inject constructor(
    private val taskFirestoreDataSource: TaskFirestoreDataSource
) : TaskRepository {

    override suspend fun addNewTask(task: Task) {
        taskFirestoreDataSource.addNewTask(task.toDto())
    }

    override suspend fun changeTaskStatus(task: Task) {
        val taskDto = task.toDto()
        taskFirestoreDataSource.changeStatus(taskDto)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskFirestoreDataSource.getAllTask()
            .map { listTaskDto ->
                listTaskDto.map { taskDto ->
                    taskDto.toTaskDomain()
                }
            }
    }

    override suspend fun deleteTask(task: Task) {
        val taskDto = task.toDto()
        taskFirestoreDataSource.deleteTask(taskDto)
    }
}