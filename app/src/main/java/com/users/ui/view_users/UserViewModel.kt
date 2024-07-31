package com.users.ui.view_users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.users.data.Result
import com.users.data.UserRepository
import com.users.data.model.DeletedUser
import com.users.data.model.User
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _deletedUser = MutableLiveData<DeletedUser>()
    val deletedUser: LiveData<DeletedUser> = _deletedUser

    private val _getUsersError = MutableLiveData<String>()
    val getUsersError: LiveData<String> = _getUsersError

    private val _errorMessage2 = MutableLiveData<String>()
    val errorMessage2: LiveData<String> = _errorMessage2

    private val _deleteError = MutableLiveData<String>()
    val deleteError: LiveData<String> = _deleteError

    suspend fun getUsers() {
        viewModelScope.launch {
            when (val result = userRepository.getUsers()) {
                is Result.Success -> {
                    _users.postValue(result.data)
                }

                is Result.Error -> {
                    _getUsersError.postValue(result.exception.message)
                }

                else -> {
                    _getUsersError.postValue("Cannot fetch users.")
                }
            }
        }
    }

    suspend fun getUser(id: String) {
        viewModelScope.launch {
            when (val result = userRepository.getUser(id)) {
                is Result.Success -> {
                    _user.postValue(result.data)
                }

                is Result.Error -> {
                    _errorMessage2.postValue(result.exception.message)
                }

                else -> {
                    _errorMessage2.postValue("Cannot fetch users.")
                }
            }
        }
    }

    suspend fun deleteUser(id: String) {
        viewModelScope.launch {
            when (val result = userRepository.deleteUser(id)) {
                is Result.Success -> {
                    _deletedUser.postValue(result.data)
                }

                is Result.Error -> {
                    _deleteError.postValue(result.exception.message)
                }

                else -> {
                    _deleteError.postValue("Cannot delete user.")
                }
            }
        }
    }
}