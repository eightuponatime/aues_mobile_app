package com.example.messenger.data

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    private lateinit var database: MyAppDatabase

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            MyAppDatabase::class.java, "app-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getDatabase(): MyAppDatabase {
        if (!::database.isInitialized) {
            throw IllegalStateException("Database is not initialized. Call initialize() first.")
        }
        return database
    }
}