package com.alezandrow.simplecleanarchitecture.data.mapper

import android.util.Log
import com.alezandrow.simplecleanarchitecture.data.source.local.ReminderEntity
import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTaskDto(): TaskDto? {
    val dto = toObject(TaskDto::class.java) ?: return null
    Log.d("DocumentSnapshotToTaskDto", "toTaskDto: $dto")
    Log.d("DocumentSnapshotToTaskDto", "toTaskDto: ${dto.id}")
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
        } catch (e: Exception) {
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
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        taskStatus = taskStatus.name
    )
}

fun TaskDto.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        dueDateTime = dueDate,
        description = description,
        status = taskStatus
    )
}
