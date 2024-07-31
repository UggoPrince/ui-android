package com.users.ui

/**
 * Authentication result : success (user details) or error message.
 */
data class UserResult(
    val success: UserView? = null,
    val error: Int? = null
)