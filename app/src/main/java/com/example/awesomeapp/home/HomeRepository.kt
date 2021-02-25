package com.example.awesomeapp.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomeapp.data.model.GetPhotosResponse
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.RemoteDataSource
import retrofit2.Call
import retrofit2.Response

class HomeRepository private constructor(private val remoteDataSource: RemoteDataSource): HomeDataSource {

    companion object {
        @Volatile
        private var instance: HomeRepository? = null
        fun getInstance(remoteData: RemoteDataSource): HomeRepository =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(remoteData)
            }
    }

    override fun getCuratedPhotos(perPage: Int): LiveData<ArrayList<Photo>> {
        val photosLiveData = MutableLiveData<ArrayList<Photo>>()
        remoteDataSource.getCuratedPhotos(perPage, object : RemoteDataSource.LoadCuratedPhotosCallback{
            override fun onResponse(
                    call: Call<GetPhotosResponse>,
                    response: Response<GetPhotosResponse>
            ) {
                val result = response.body()
                if(result != null){
                    photosLiveData.postValue(result.photos)
                }
            }

            override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
                Log.d("onFailure", t.localizedMessage)
            }

        })
        return photosLiveData
    }

    override fun getSearchedPhotos(query: String): LiveData<ArrayList<Photo>> {
        val photosLiveData = MutableLiveData<ArrayList<Photo>>()
        remoteDataSource.getSearchedPhotos(query, object : RemoteDataSource.LoadSearchedPhotosCallback{
            override fun onResponse(
                    call: Call<GetPhotosResponse>,
                    response: Response<GetPhotosResponse>
            ) {
                val result = response.body()
                if(result != null){
                    photosLiveData.postValue(result.photos)
                }
            }

            override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
                Log.d("onFailure", t.localizedMessage)
            }

        })
        return photosLiveData
    }


}