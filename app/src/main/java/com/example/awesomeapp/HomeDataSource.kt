package com.example.awesomeapp

import androidx.lifecycle.LiveData
import com.example.awesomeapp.model.Photo

interface HomeDataSource {

    fun getCuratedPhotos(perPage:Int): LiveData<ArrayList<Photo>>
    fun getSearchedPhotos(query: String): LiveData<ArrayList<Photo>>
}