package com.alezandrow.simplecleanarchitecture.domain.entities

data class Task (
    val id: Int, val description: String, val status: TaskStatus
)
