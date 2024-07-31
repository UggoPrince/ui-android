package com.users.di

import com.users.service.UserService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        val baseUrl = "https://u-management.free.beeceptor.com/"
        // val apiService: UserService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return@single retrofit.create(UserService::class.java)
        // }
    }
}
