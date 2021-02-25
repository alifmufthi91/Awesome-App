package com.example.awesomeapp.data.model

import com.google.gson.annotations.SerializedName

data class GetPhotosResponse(
    @SerializedName("page")
    var page: Int,
    @SerializedName("per_page")
    var perPage: Int,
    @SerializedName("next_page")
    var nextPage: String,
    @SerializedName("photos")
    var photos: ArrayList<Photo>
)
