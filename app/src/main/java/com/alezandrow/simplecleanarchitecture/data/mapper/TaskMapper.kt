package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTaskDto(): TaskDto? {
    val dto = toObject(TaskDto::class.java) ?: return null
    return dto.copy(id = id)
}

fun TaskDto.toTaskDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        priority = try {
            TaskPriority.valueOf(priority)
        } catch (e: Exception) {
            TaskPriority.LOW
        },
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        taskStatus = try {
            TaskStatus.valueOf(taskStatus)
        } catch(e: Exception) {
            TaskStatus.NEW
        }
    )
}

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        priority = priority.name,
        dueDate = dueDate ?: System.currentTimeMillis(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        taskStatus = taskStatus.name
    )
}