package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDao
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun addNewTask(task: Task) {
//        val dbEntity = task.toDbEntity()
//        taskDao.addNewTask(dbEntity)
    }

    override suspend fun changeTaskStatus(task: Task) {
//        val dbEntity = task.toDbEntity()
//        taskDao.changeTaskStatus(dbEntity)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return flow {
            emit(listOf(Task(status = TaskStatus.NEW, description = "Test")))
        }

    }

    override suspend fun deleteTask(task: Task) {
//        val dbEntity = task.toDbEntity()
//        taskDao.deleteTask(dbEntity)
    }
}