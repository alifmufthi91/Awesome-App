package com.example.awesomeapp.networking

import com.example.awesomeapp.model.GetPhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("curated")
    fun curated(@Header("Authorization") auth: String, @Query("per_page") perPage: Int): Call<GetPhotosResponse>

    @GET("search")
    fun search(@Header("Authorization") auth: String, @Query("query") query: String): Call<GetPhotosResponse>
}