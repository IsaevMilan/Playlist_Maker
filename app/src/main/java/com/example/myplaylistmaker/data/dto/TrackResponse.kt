package com.example.myplaylistmaker.data.dto

import com.example.myplaylistmaker.domain.models.Track

data class TrackResponse(

    val resultCount: Int,
    val results: ArrayList<Track>
) : Response()

