package com.alezandrow.simplecleanarchitecture.domain.entities

data class Note(
    val id: Int, val description: String, val status: NoteStatus
)
