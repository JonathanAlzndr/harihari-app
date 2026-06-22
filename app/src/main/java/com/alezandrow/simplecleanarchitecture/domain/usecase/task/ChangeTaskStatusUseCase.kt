package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class ChangeTaskStatusUseCase @Inject constructor (private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.changeTaskStatus(task)
}