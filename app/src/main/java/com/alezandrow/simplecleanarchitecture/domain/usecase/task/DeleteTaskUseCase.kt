package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.deleteTask(taskId)
}