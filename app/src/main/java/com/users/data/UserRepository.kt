package com.users.data

import com.users.data.model.User

class UserRepository(private val dataSource: UserDataSource) {
    suspend fun addUser(email: String, firstname: String, lastname: String): Result<User> {
        val result = dataSource.addUser(email, firstname, lastname)
        return result
    }

    suspend fun getUsers(): Result<List<User>> {
        return dataSource.getUsers()
    }

    suspend fun getUser(id: String): Result<User> {
        return dataSource.getUser(id)
    }

    suspend fun updateUser(user: User): Result<User> {
        val result = dataSource.updateUser(user)
        return result
    }
}