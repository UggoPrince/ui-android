package com.users.ui.update_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.users.data.Result

import com.users.R
import com.users.data.UserRepository
import com.users.data.model.User
import com.users.ui.UserResult
import com.users.ui.UserView
import com.users.util.isEmailValid
import com.users.util.isNameValid
import com.users.util.isNotEmpty
import kotlinx.coroutines.launch

class UpdateUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _updateUserForm = MutableLiveData<UpdateUserFormState>()
    val updateUserFormState: LiveData<UpdateUserFormState> = _updateUserForm

    private val _updateUserResult = MutableLiveData<UserResult>()
    val updateUserResult: LiveData<UserResult> = _updateUserResult

    suspend fun updateUser(user: User) {
        viewModelScope.launch {
            val result = userRepository.updateUser(user)

            if (result is Result.Success) {
                _updateUserResult.value =
                    UserResult(success = UserView(displayName = result.data.firstName + ' ' + result.data.lastName))
            } else {
                _updateUserResult.value = UserResult(error = R.string.adding_user_failed)
            }
        }
    }

    fun addUserDataChanged(email: String, firstname: String, lastname: String) {
        if (isNotEmpty(firstname) && isNotEmpty(lastname) && isNotEmpty(email)) {
            _updateUserForm.postValue(UpdateUserFormState(allFieldsFilled = true))
        } else {
            _updateUserForm.postValue(UpdateUserFormState(allFieldsFilled = false))
        }
    }

    fun canAddUserCheck(email: String, firstname: String, lastname: String) {
        if (!isEmailValid(email)) {
            _updateUserForm.value = UpdateUserFormState(emailError = R.string.invalid_email)
        } else if (!isNameValid(firstname)) {
            _updateUserForm.value = UpdateUserFormState(firstnameError = R.string.invalid_firstname)
        } else if (!isNameValid(lastname)) {
            _updateUserForm.value = UpdateUserFormState(lastnameError = R.string.invalid_lastname)
        } else {
            _updateUserForm.value = UpdateUserFormState(isDataValid = true)
        }
    }
}