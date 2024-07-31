package com.users.ui.add_user

/**
 * Data validation state of the login form.
 */
data class AddUserFormState(
    val emailError: Int? = null,
    val firstnameError: Int? = null,
    val lastnameError: Int? = null,
    var isDataValid: Boolean = false,
    var allFieldsFilled: Boolean = false
)