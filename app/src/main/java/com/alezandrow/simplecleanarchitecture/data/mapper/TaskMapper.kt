package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

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
        dueDate = dueDate ?: System.currentTimeMillis(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        taskStatus = taskStatus.name
    )
}

object FirestoreErrorMapper {
    fun map(e: Exception): AppError {
        return when (e) {
            is FirebaseFirestoreException -> {
                when (e.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        AppError.PermissionDenied

                    FirebaseFirestoreException.Code.NOT_FOUND ->
                        AppError.NotFound

                    FirebaseFirestoreException.Code.ALREADY_EXISTS ->
                        AppError.AlreadyExists

                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        AppError.Network

                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
                        AppError.Timeout

                    FirebaseFirestoreException.Code.CANCELLED ->
                        AppError.Cancelled

                    FirebaseFirestoreException.Code.ABORTED ->
                        AppError.OperationAborted

                    FirebaseFirestoreException.Code.UNAUTHENTICATED ->
                        AppError.Unauthenticated

                    else ->
                        AppError.Unknown(e.message ?: "Unknown Firestore error")
                }
            }
            else -> AppError.Unknown(e.message ?: "Unknown Firestore error")
        }
    }
}
