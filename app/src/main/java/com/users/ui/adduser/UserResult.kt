package com.users.ui.adduser

/**
 * Authentication result : success (user details) or error message.
 */
data class UserResult(
    val success: AddedUserView? = null,
    val error: Int? = null
)