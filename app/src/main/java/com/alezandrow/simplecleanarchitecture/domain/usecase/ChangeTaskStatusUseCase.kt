package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository

class ChangeTaskStatusUseCase(private val repository: ITaskRepository) {
    suspend operator fun invoke(task: Task) = repository.changeTaskStatus(task)
}