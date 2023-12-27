package com.codegama.todolistapplication.database

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.codegama.todolistapplication.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataBaseAction(): OnDataBaseAction
    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        return null
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return null
    }

    override fun clearAllTables() {}

    companion object {
        @Volatile
        private val appDatabase: AppDatabase? = null
    }
}