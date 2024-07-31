package com.users.service

import com.users.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("/api/users/")
    suspend fun addUser(@Body user: User): User

    @GET("/api/users")
    suspend fun getUsers(): List<User>

    @GET("/api/users/{id}")
    suspend fun getUser(@Path("id") id: String): User

    @PATCH("/api/users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User
}