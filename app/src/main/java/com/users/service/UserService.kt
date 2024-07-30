package com.users.service

import com.users.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("/api/users/")
    suspend fun addUser(@Body user: User): User

    @GET("/api/users")
    suspend fun getUsers(): List<User>
}