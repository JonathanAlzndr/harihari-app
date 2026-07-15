package com.alezandrow.simplecleanarchitecture.domain.entities.task

data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    val dueDate: Long?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val priority: TaskPriority,
    val taskStatus: TaskStatus
)