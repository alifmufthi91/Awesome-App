package com.example.awesomeapp.data.source

import com.example.awesomeapp.data.model.GetPhotosResponse
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.utils.DummyApiResponse
import com.google.gson.Gson

class LocalMain {
    fun getSearchedPhotos(): ArrayList<Photo>{
        val gson = Gson()
        return gson.fromJson(DummyApiResponse().photoResponse, GetPhotosResponse::class.java).photos
    }
}