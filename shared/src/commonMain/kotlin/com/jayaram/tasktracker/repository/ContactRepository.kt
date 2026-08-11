package com.jayaram.tasktracker.repository

import com.jayaram.tasktracker.database.AppDatabase

class ContactRepository(
    private val database: AppDatabase
) {

    private val queries = database.appDatabaseQueries

    fun insertSubmission(
        name: String,
        email: String,
        message: String
    ) {
        queries.insertSubmission(
            name,
            email,
            message
        )
    }

    fun getAllSubmissions() =
        queries.getAllSubmissions().executeAsList()

    fun deleteSubmission(
        id: Long
    ) {
        queries.deleteSubmission(id)
    }

    fun deleteAll() {
        queries.deleteAllSubmissions()
    }

    fun getSubmissionCount(): Long {

        val count = queries
            .getSubmissionCount()
            .executeAsOne()

        println("ContactForm: Submission Count = $count")

        return count
    }
}