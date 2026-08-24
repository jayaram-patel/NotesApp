package com.jayaram.tasktracker.repository

import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.model.Note

class NoteRepository(
    private val database: AppDatabase
) {

    private val queries = database.appDatabaseQueries

    fun addNote(folderId: Long, text: String) {
        val maxPos = queries
            .getMaxPosition(folderId)
            .executeAsOneOrNull()?.maxPos ?: -1L
        queries.insertNote(
            folderId = folderId,
            text = text,
            position = maxPos + 1
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
                    folderId = it.folderId,
                    position = it.position.toInt()
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
                    folderId = it.folderId,
                    position = it.position.toInt()
                )
            }
    }

    fun updateNotePosition(id: Long, position: Int){
        queries.updateNotePosition(position.toLong(), id)
    }
}