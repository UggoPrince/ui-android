package com.users.ui.viewusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.users.data.Result
import com.users.data.UserRepository
import com.users.data.model.User
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun getUsers() {
        viewModelScope.launch {
            when (val result = userRepository.getUsers()) {
                is Result.Success -> {
                    _users.postValue(result.data)
                }

                is Result.Error -> {
                    _errorMessage.postValue(result.exception.message)
                }

                else -> {
                    _errorMessage.postValue("Cannot fetch users.")
                }
            }
        }
    }
}