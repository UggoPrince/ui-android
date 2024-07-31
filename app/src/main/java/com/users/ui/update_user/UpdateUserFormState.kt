package com.users.ui.update_user

/**
 * Data validation state of the login form.
 */
data class UpdateUserFormState(
    val emailError: Int? = null,
    val firstnameError: Int? = null,
    val lastnameError: Int? = null,
    var isDataValid: Boolean = false,
    var allFieldsFilled: Boolean = false
)