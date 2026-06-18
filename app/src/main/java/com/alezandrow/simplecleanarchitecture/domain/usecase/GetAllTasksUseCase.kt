package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository

class GetAllTasksUseCase(private val repository: ITaskRepository) {
    suspend operator fun invoke() = repository.getAllTasks()
}