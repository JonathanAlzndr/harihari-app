package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("reminder")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val dueDateTime: Long,
    val description: String,
    val status: String
)
