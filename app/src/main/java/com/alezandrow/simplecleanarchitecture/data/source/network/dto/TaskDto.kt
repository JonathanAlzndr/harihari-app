package com.alezandrow.simplecleanarchitecture.data.source.network.dto

data class TaskDto(
    val id: String = "",
    val title: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val dueDate: Long = 0L,
    val description: String = "",
    val priority: String = "",
    val taskStatus: String = ""
)
