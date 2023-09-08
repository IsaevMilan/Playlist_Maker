package com.example.myplaylistmaker.data.network


import com.example.myplaylistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchAPI {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String): Call<TrackResponse>
}