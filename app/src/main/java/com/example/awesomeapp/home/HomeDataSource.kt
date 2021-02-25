package com.example.awesomeapp.home

import androidx.lifecycle.LiveData
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.ApiResponse

//Interface untuk HomeRepository
interface HomeDataSource {

    fun getCuratedPhotos(perPage:Int): LiveData<ApiResponse>
    fun getSearchedPhotos(query: String, page: Int): LiveData<ApiResponse>
}