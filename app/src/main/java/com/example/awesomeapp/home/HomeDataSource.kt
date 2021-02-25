package com.example.awesomeapp.home

import androidx.lifecycle.LiveData
import com.example.awesomeapp.data.model.Photo

interface HomeDataSource {

    fun getCuratedPhotos(perPage:Int): LiveData<ArrayList<Photo>>
    fun getSearchedPhotos(query: String): LiveData<ArrayList<Photo>>
}