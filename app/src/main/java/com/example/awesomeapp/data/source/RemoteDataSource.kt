package com.example.awesomeapp.data.source

import com.example.awesomeapp.BuildConfig
import com.example.awesomeapp.data.model.GetPhotosResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    //Inisialisasi retrofit client
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    private val builder = Retrofit.Builder()
        .baseUrl("https://api.pexels.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = builder.build()
    private val client: ApiService = retrofit.create(ApiService::class.java)


    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null
        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource()
            }
    }

    fun getSearchedPhotos(query: String, page: Int, callback: LoadSearchedPhotosCallback){
        val call = client.search(BuildConfig.API_KEY, query, page)
        call.enqueue(callback)
    }

    fun getCuratedPhotos(perPage: Int, callback: LoadCuratedPhotosCallback){
        val call = client.curated(BuildConfig.API_KEY, perPage)
        call.enqueue(callback)
    }

    interface LoadSearchedPhotosCallback : Callback<GetPhotosResponse>
    interface LoadCuratedPhotosCallback : Callback<GetPhotosResponse>
}