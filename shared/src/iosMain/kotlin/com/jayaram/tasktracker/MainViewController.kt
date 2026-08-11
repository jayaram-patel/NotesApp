package com.jayaram.tasktracker

import androidx.compose.ui.window.ComposeUIViewController
import com.jayaram.tasktracker.repository.FolderRepository
import com.jayaram.tasktracker.repository.NoteRepository
import com.jayaram.tasktracker.repository.ContactRepository

fun MainViewController() = ComposeUIViewController {

    val database = DatabaseModule.provideDatabase()

    val folderRepository = FolderRepository(database)
    val noteRepository = NoteRepository(database)
    val contactRepository = ContactRepository(database)

    App(
        folderRepository = folderRepository,
        noteRepository = noteRepository,
        contactRepository = contactRepository // ADD THIS PARAMETER
    )
}