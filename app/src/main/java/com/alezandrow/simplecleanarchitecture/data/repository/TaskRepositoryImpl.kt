package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.mapper.toDbEntity
import com.alezandrow.simplecleanarchitecture.data.mapper.toDomain
import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDao
import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl (private val taskDao: TaskDao) : ITaskRepository {
    override suspend fun addNewTask(task: Task) {
        val dbEntity = task.toDbEntity()
        taskDao.addNewTask(dbEntity)
    }

    override suspend fun changeTaskStatus(task: Task) {
        val dbEntity = task.toDbEntity()
        taskDao.changeTaskStatus(dbEntity)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { listFromDb ->
            listFromDb.map { it.toDomain() }
        }
    }
}