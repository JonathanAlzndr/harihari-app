package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDbEntity
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus

fun TaskDbEntity.toDomain(): Task {
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

fun Task.toDbEntity(): TaskDbEntity {
    return TaskDbEntity(
        id = id,
        description = description,
        status = status.name
    )
}