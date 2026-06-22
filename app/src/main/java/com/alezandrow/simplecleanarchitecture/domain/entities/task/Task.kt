package com.alezandrow.simplecleanarchitecture.domain.entities.task

data class Task(
    val id: Int = 0,
    val description: String,
    val status: TaskStatus
)