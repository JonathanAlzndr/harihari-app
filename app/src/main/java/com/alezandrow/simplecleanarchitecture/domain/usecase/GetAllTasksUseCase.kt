package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository

class GetAllTasksUseCase(private val repository: ITaskRepository) {
   operator fun invoke() = repository.getAllTasks()
}