package com.users.di

import com.users.data.UserDataSource
import com.users.data.UserRepository
import com.users.ui.adduser.AddUserViewModel
import com.users.ui.viewusers.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AddUserViewModel(get()) }
    viewModel { UserViewModel(get()) }
    single { UserRepository(get()) }
    single { UserDataSource(get()) }
}