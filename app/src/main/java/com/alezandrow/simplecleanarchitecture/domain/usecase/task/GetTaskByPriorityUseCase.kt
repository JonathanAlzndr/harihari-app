package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByPriorityUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(priority: TaskPriority) =
        taskRepository.getTasksByPriority(priority)
}