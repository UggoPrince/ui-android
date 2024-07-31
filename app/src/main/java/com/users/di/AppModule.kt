package com.users.di

import com.users.data.UserDataSource
import com.users.data.UserRepository
import com.users.ui.add_user.AddUserViewModel
import com.users.ui.update_user.UpdateUserViewModel
import com.users.ui.view_users.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AddUserViewModel(get()) }
    viewModel { UpdateUserViewModel(get()) }
    viewModel { UserViewModel(get()) }
    single { UserRepository(get()) }
    single { UserDataSource(get()) }
}