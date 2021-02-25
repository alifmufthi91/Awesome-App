package com.example.awesomeapp.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("id")
    var id: Int,
        @SerializedName("url")
    var url: String,
        @SerializedName("photographer")
    var photographer: String,
        @SerializedName("src")
    var src: Source,
        @SerializedName("avg_color")
    var avgColor: String,
){
    class Source(
        @SerializedName("original")
        var original: String,
        @SerializedName("large")
        var large: String,
        @SerializedName("medium")
        var medium: String,
        @SerializedName("portrait")
        var portrait: String,
        @SerializedName("landscape")
        var landscape: String
    )
}
