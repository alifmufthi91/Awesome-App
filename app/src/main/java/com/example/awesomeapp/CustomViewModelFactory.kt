package com.example.awesomeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.awesomeapp.home.HomeRepository
import com.example.awesomeapp.home.HomeViewModel

class CustomViewModelFactory private constructor(private val homeRepository: HomeRepository):  ViewModelProvider.NewInstanceFactory(){

    companion object {
        @Volatile
        private var instance: CustomViewModelFactory? = null

        fun getInstance(): CustomViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: CustomViewModelFactory(Injection.provideRepository())
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(homeRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}