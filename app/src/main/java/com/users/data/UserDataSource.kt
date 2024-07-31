package com.users.data

import com.users.data.model.DeletedUser
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

    suspend fun getUser(id: String): Result<User> {
        return try {
            val user = apiService.getUser(id)
            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(IOException("Error fetching user.", e))
        }
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            val update = apiService.updateUser(user.id, user)
            Result.Success(update)
        } catch (e: Throwable) {
            Result.Error(IOException("Error adding user.", e))
        }
    }

    suspend fun deleteUser(id: String): Result<DeletedUser> {
        return try {
            val deleted = apiService.deleteUser(id)
            Result.Success(deleted)
        } catch (e: Throwable) {
            Result.Error(IOException("Error adding user.", e))
        }
    }
}