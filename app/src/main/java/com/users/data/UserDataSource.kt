package com.users.data

import android.util.Log
import com.users.data.model.User
import com.users.service.UserService
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class UserDataSource(private val apiService: UserService) {

    suspend fun addUser(email: String, firstname: String, lastname: String): Result<User> {
        return try {
            val user = User(java.util.UUID.randomUUID().toString(), firstname, lastname, email)
            val newUser = apiService.addUser(user)
            Result.Success(newUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error adding user.", e))
        }
    }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = apiService.getUsers()
            Result.Success(users)
        } catch (e: Throwable) {
            Result.Error(IOException("Error fetching users.", e))
        }
    }
}