package com.jayaram.tasktracker.model

data class Note(
    val id: Long,
    val text: String,
    val folderId: Long
)