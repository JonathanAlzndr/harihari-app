package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor (private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): AppResult<Unit> {
        return repository.addNewTask(task)
    }
}