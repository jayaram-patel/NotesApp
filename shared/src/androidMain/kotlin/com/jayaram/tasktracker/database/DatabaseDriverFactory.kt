package com.jayaram.tasktracker.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "notes.db",
            callback = object : AndroidSqliteDriver.Callback(AppDatabase.Schema) {
                override fun onDowngrade(
                    db: SupportSQLiteDatabase,
                    oldVersion: Int,
                    newVersion: Int
                ) {
                    db.execSQL("DROP TABLE IF EXISTS Note")
                    db.execSQL("DROP TABLE IF EXISTS Folder")
                    db.execSQL("DROP TABLE IF EXISTS ContactSubmission")
                    onCreate(db)
                }
            }
        )
    }
}
