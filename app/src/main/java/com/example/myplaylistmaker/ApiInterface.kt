package com.example.myplaylistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/search?entity=song")
    fun findMedia(@Query("term") text: String): Call<MediaResponse>




}



