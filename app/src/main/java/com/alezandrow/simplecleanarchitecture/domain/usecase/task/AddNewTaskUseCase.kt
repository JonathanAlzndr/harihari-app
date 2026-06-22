package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor (private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addNewTask(task)
}