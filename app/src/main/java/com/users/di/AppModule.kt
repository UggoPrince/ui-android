package com.users.di

import com.users.data.UserDataSource
import com.users.data.UserRepository
import com.users.ui.adduser.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { UserViewModel(get()) }
    single { UserRepository(get()) }
    single { UserDataSource(get()) }
}