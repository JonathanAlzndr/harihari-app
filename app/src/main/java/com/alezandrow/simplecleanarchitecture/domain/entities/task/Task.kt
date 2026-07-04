package com.alezandrow.simplecleanarchitecture.domain.entities.task

data class Task(
    val id: String = "",
    val description: String,
    val status: TaskStatus
)