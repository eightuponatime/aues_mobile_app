package com.example.messenger.data

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    private lateinit var database: AppDatabase

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }

    fun getDatabase(): AppDatabase {
        if (!::database.isInitialized) {
            throw IllegalStateException("Database is not initialized. Call initialize() first.")
        }
        return database
    }
}