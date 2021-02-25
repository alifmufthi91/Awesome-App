package com.example.awesomeapp.data.source

import com.example.awesomeapp.data.model.Photo

//Membungkus response dari api untuk
//mengetahui apakah panggilan api
//berhasil atau gagal
class ApiResponse {
    var data: ArrayList<Photo>?
    var error: Throwable?

    constructor(data: ArrayList<Photo>){
        this.data = data
        this.error = null
    }

    constructor(error: Throwable){
        this.error = error
        this.data = null
    }
}