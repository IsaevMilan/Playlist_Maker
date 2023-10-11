package com.example.myplaylistmaker.data.search.request_and_response



data class TrackResponse(

    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()

