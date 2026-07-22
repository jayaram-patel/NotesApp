package com.jayaram.tasktracker.repository

import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.model.Folder

class FolderRepository(
    private val database: AppDatabase
) {

    private val queries = database.appDatabaseQueries

    fun addFolder(name: String) {
        queries.insertFolder(name)
    }

    fun getFolders(): List<Folder> {
        return queries
            .getFolders()
            .executeAsList()
            .map {
                Folder(
                    id = it.id,
                    name = it.name
                )
            }
    }

    fun deleteFolder(id: Long) {
        queries.deleteFolder(id)
    }

    fun updateFolder(folder: Folder) {
        queries.updateFolder(
            name = folder.name,
            id = folder.id
        )
    }
}