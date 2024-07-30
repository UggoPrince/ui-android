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
}