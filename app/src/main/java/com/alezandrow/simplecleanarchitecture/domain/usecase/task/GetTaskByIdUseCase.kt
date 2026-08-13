package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: String): AppResult<Task> = taskRepository.getTaskById(taskId)
}