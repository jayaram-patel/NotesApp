package com.jayaram.tasktracker

import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.database.DatabaseDriverFactory

object DatabaseModule {

    fun provideDatabase(): AppDatabase {
        return AppDatabase(
            DatabaseDriverFactory().createDriver()
        )
    }
}