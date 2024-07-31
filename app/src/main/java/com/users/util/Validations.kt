package com.users.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

fun isEmailValid(email: String): Boolean {
    return if (email.contains('@')) {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } else {
        false
        // email.isNotBlank()
    }
}

// field is not empty validation
fun isNotEmpty(str: String): Boolean {
    return str.isNotBlank()
}

// A placeholder password validation check
fun isNameValid(name: String): Boolean {
    return name.length > 2
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
