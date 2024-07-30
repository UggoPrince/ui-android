package com.users.ui

import com.users.ui.adduser.AddedUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class UserResult(
    val success: AddedUserView? = null,
    val error: Int? = null
)