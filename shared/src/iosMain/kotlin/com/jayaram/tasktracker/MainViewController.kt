package com.jayaram.tasktracker

import androidx.compose.ui.window.ComposeUIViewController
import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.database.DatabaseDriverFactory
import com.jayaram.tasktracker.repository.FolderRepository
import com.jayaram.tasktracker.repository.NoteRepository

fun MainViewController() = ComposeUIViewController {

    val database = DatabaseModule.provideDatabase()

    val folderRepository = FolderRepository(database)
    val noteRepository = NoteRepository(database)

    App(
        folderRepository = folderRepository,
        noteRepository = noteRepository
    )
}