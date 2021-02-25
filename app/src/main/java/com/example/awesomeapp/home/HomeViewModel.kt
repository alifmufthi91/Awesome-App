package com.example.awesomeapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.ApiResponse
import com.example.awesomeapp.data.source.RemoteDataSource

//View model untuk fragment home
class HomeViewModel(private var homeRepository: HomeRepository): ViewModel() {

    var currentPage = 1
    var isListMode: Boolean = true

    internal fun getCuratedPhotos(perPage: Int): LiveData<ApiResponse>{
        return homeRepository.getCuratedPhotos(perPage)
    }

    internal fun getSearchedPhotos(query: String, page: Int): LiveData<ApiResponse>{
        return homeRepository.getSearchedPhotos(query, page)
    }

    internal fun getTodayPhoto(): LiveData<ApiResponse>{
        return getCuratedPhotos(1)
    }

}