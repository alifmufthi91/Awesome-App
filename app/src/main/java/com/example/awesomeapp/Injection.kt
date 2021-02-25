package com.example.awesomeapp

import com.example.awesomeapp.data.source.RemoteDataSource
import com.example.awesomeapp.home.HomeRepository

object Injection {
    fun provideRepository(): HomeRepository {

        val remoteDataSource = RemoteDataSource.getInstance()

        return HomeRepository.getInstance(remoteDataSource)
    }
}