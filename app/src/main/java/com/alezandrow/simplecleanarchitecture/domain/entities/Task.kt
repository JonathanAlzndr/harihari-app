package com.alezandrow.simplecleanarchitecture.domain.entities

data class Task(
    val id: Int = 0,
    val description: String,
    val status: TaskStatus
)
