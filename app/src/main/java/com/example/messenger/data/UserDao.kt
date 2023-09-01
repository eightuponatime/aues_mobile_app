package com.example.messenger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity)
}
