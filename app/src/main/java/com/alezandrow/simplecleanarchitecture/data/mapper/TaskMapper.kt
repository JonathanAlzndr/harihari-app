package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.data.source.local.TaskDbEntity
import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.TaskStatus

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
        description = description,
        status = status.name
    )
}