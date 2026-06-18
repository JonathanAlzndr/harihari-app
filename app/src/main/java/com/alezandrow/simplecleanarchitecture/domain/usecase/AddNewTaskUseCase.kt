package com.alezandrow.simplecleanarchitecture.domain.usecase

import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository

class AddNewTaskUseCase (private val repository: ITaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addNewTask(task)
}