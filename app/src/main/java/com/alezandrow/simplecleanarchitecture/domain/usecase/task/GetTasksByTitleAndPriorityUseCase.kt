package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksByTitleAndPriorityUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(title: String, priority: TaskPriority?): Flow<AppResult<List<Task>>> =
        taskRepository.getTasksByTitleAndPriority(title, priority)
}