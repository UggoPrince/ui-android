package com.users.ui.adduser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.users.data.Result

import com.users.R
import com.users.data.UserRepository
import com.users.ui.UserResult
import kotlinx.coroutines.launch

class AddUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _addUserForm = MutableLiveData<AddUserFormState>()
    val addUserFormState: LiveData<AddUserFormState> = _addUserForm

    private val _addUserResult = MutableLiveData<UserResult>()
    val addUserResult: LiveData<UserResult> = _addUserResult

    suspend fun addUser(email: String, firstname: String, lastname: String) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result = userRepository.addUser(email, firstname, lastname)

            if (result is Result.Success) {
                _addUserResult.value =
                    UserResult(success = AddedUserView(displayName = result.data.firstName + ' ' + result.data.lastName))
            } else {
                _addUserResult.value = UserResult(error = R.string.adding_user_failed)
            }
        }
    }

    fun addUserDataChanged(email: String, firstname: String, lastname: String) {
        if (isNotEmpty(firstname) && isNotEmpty(lastname) && isNotEmpty(email)) {
            _addUserForm.postValue(AddUserFormState(allFieldsFilled = true))
        } else {
            _addUserForm.postValue(AddUserFormState(allFieldsFilled = false))
        }
    }

    fun canAddUserCheck(email: String, firstname: String, lastname: String) {
        Log.d("VALID :", isEmailValid(email).toString())
        if (!isEmailValid(email)) {
            _addUserForm.value = AddUserFormState(emailError = R.string.invalid_email)
        } else if (!isNameValid(firstname)) {
            _addUserForm.value = AddUserFormState(firstnameError = R.string.invalid_firstname)
        } else if (!isNameValid(lastname)) {
            _addUserForm.value = AddUserFormState(lastnameError = R.string.invalid_lastname)
        } else {
            _addUserForm.value = AddUserFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
            // email.isNotBlank()
        }
    }

    // field is not empty validation
    private fun isNotEmpty(str: String): Boolean {
        return str.isNotBlank()
    }

    // A placeholder password validation check
    private fun isNameValid(name: String): Boolean {
        return name.length > 2
    }
}