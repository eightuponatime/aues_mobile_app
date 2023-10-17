package com.example.messenger.network

import com.google.gson.annotations.SerializedName
import java.sql.Blob

data class PasswordRequest(
    @SerializedName("username") val username: String
)

data class StudentRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("study_group") val study_group: String
)

data class ExistanceRequest(
    @SerializedName("username") val username: String
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class ParseRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class UserRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class UsernameRequest(
    @SerializedName("username") val username: String
)

data class SetImageRequest(
    @SerializedName("username") val username: String,
    @SerializedName("image") val image: ByteArray
)

data class GetImageRequest(
    @SerializedName("username") val username: String
)