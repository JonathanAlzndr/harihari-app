package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.mapper.toDbEntity
import com.alezandrow.simplecleanarchitecture.data.mapper.toDomain
import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDao
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
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

    override suspend fun deleteTask(task: Task) {
        val dbEntity = task.toDbEntity()
        taskDao.deleteTask(dbEntity)
    }
}