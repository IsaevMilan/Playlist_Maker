package com.example.myplaylistmaker.data.search.request_and_response

data class TrackDto (
    val trackName: String,
    val addTime: Long?,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {

}
