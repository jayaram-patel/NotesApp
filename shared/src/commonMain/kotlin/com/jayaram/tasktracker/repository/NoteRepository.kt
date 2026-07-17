package com.jayaram.tasktracker.repository

import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.database.DatabaseDriverFactory
import com.jayaram.tasktracker.model.Note

class NoteRepository(
    databaseDriverFactory: DatabaseDriverFactory
) {

    private val database = AppDatabase(
        databaseDriverFactory.createDriver()
    )

    private val queries = database.appDatabaseQueries

    fun addNote(folderId: Long, text: String) {
        queries.insertNote(
            folderId = folderId,
            text = text
        )
    }

    fun getNotes(folderId: Long): List<Note> {
        return queries
            .getNotes(folderId)
            .executeAsList()
            .map {
                Note(
                    id = it.id,
                    text = it.text,
                    folderId = it.folderId
                )
            }
    }

    fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    fun updateNote(note: Note) {
        queries.updateNote(
            text = note.text,
            id = note.id
        )
    }

    fun getAllNotes(): List<Note> {
        return queries
            .getAllNotes()
            .executeAsList()
            .map {
                Note(
                    id = it.id,
                    text = it.text,
                    folderId = it.folderId
                )
            }
    }

}