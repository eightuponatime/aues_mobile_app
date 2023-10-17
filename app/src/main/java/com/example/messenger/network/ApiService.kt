package com.example.messenger.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/")
    suspend fun checkServerStatus(): String

    @GET("/getAllUsersFromDatabase")
    suspend fun getAllUsersFromDatabase(): List<Student>

    @POST("/addUser")
    suspend fun addUser(@Body studentRequest: StudentRequest): Boolean

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Boolean

    @POST("/parse-schedule")
    suspend fun parseSchedule(@Body parseRequest: ParseRequest): String

    @POST("/getPassword")
    suspend fun getPassword(@Body passwordRequest: PasswordRequest): String

    @POST("/checkIfUserExists")
    suspend fun checkIfUserExists(@Body existanceRequest: ExistanceRequest): Boolean

    @POST("/getGroup")
    suspend fun getGroup(@Body usernameRequest: UsernameRequest): String

    @POST("/getFirstName")
    suspend fun getFirstName(@Body usernameRequest: UsernameRequest): String

    @POST("/getLastName")
    suspend fun getLastName(@Body usernameRequest: UsernameRequest): String

    @POST("/setImage")
    suspend fun setImage(@Body setImageRequest: SetImageRequest): Boolean

    @POST("/getImage")
    suspend fun getImage(@Body getImageRequest: GetImageRequest): ByteArray
}