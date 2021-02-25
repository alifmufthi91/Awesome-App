package com.example.awesomeapp.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomeapp.data.model.GetPhotosResponse
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.ApiResponse
import com.example.awesomeapp.data.source.RemoteDataSource
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response

// Repository untuk fragment Home
class HomeRepository private constructor(private val remoteDataSource: RemoteDataSource): HomeDataSource {

    companion object {
        @Volatile
        private var instance: HomeRepository? = null
        fun getInstance(remoteData: RemoteDataSource): HomeRepository =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(remoteData)
            }
    }

    override fun getCuratedPhotos(perPage: Int): LiveData<ApiResponse> {
        val photosLiveData = MutableLiveData<ApiResponse>()
        remoteDataSource.getCuratedPhotos(perPage, object : RemoteDataSource.LoadCuratedPhotosCallback{
            override fun onResponse(
                    call: Call<GetPhotosResponse>,
                    response: Response<GetPhotosResponse>
            ) {
                val result = response.body()
                if(result != null){
                    photosLiveData.postValue(ApiResponse(result.photos))
                }
            }

            override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
                Log.d("onFailure", t.localizedMessage)
                photosLiveData.postValue(ApiResponse(t))
            }

        })
        return photosLiveData
    }

    override fun getSearchedPhotos(query: String, page: Int): LiveData<ApiResponse> {
        val photosLiveData = MutableLiveData<ApiResponse>()
        remoteDataSource.getSearchedPhotos(query, page, object : RemoteDataSource.LoadSearchedPhotosCallback{
            override fun onResponse(
                    call: Call<GetPhotosResponse>,
                    response: Response<GetPhotosResponse>
            ) {
                val result = response.body()
                if(result != null){
                    photosLiveData.postValue(ApiResponse(result.photos))
                }
            }

            override fun onFailure(call: Call<GetPhotosResponse>, t: Throwable) {
                Log.d("onFailure", t.localizedMessage)
                photosLiveData.postValue(ApiResponse(t))
            }

        })
        return photosLiveData
    }


}