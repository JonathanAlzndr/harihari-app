package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTaskDto(): TaskDto? {
    val dto = toObject(TaskDto::class.java) ?: return null
    return dto.copy(id = id)
}

fun TaskDto.toTaskDomain(): Task {
    return Task(
        id = id,
        description = description,
        status = try {
            TaskStatus.valueOf(status)
        } catch(e: Exception) {
            TaskStatus.NEW
        }
    )
}

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id,
        description = description,
        status = status.name
    )
}