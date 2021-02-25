package com.example.awesomeapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.RemoteDataSource

class HomeViewModel: ViewModel() {

    var isListMode: Boolean = true
    private val remoteDataSource = RemoteDataSource.getInstance()
    private val homeRepository: HomeRepository = HomeRepository.getInstance(remoteDataSource)

    internal fun getCuratedPhotos(perPage: Int): LiveData<ArrayList<Photo>>{
        return homeRepository.getCuratedPhotos(perPage)
    }

    internal fun getSearchedPhotos(query: String): LiveData<ArrayList<Photo>>{
        return homeRepository.getSearchedPhotos(query)
    }

    internal fun getTodayPhoto(): LiveData<ArrayList<Photo>>{
        return getCuratedPhotos(1)
    }
}