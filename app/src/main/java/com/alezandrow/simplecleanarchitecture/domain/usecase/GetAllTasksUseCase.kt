package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val repository: ITaskRepository) {
   operator fun invoke() = repository.getAllTasks()
}