package com.alezandrow.simplecleanarchitecture.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("task")
data class TaskDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val status: String
)
