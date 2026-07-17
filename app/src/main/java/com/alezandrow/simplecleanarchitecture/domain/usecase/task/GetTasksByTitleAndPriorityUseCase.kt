package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksByTitleAndPriorityUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(title: String, priority: TaskPriority?) =
        taskRepository.getTaskByTitleAndPriority(title, priority)
}