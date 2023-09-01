package com.example.messenger.network

import com.example.messenger.data.UserEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body user: UserEntity): ResponseBody

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>
}