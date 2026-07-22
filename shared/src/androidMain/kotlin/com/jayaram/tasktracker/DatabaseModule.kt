package com.jayaram.tasktracker

import android.content.Context
import com.jayaram.tasktracker.database.AppDatabase
import com.jayaram.tasktracker.database.DatabaseDriverFactory

object DatabaseModule {

    fun provideDatabase(
        context: Context
    ): AppDatabase {

        return AppDatabase(
            DatabaseDriverFactory(context)
                .createDriver()
        )
    }
}