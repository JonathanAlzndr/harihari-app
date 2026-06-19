package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: ITaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}