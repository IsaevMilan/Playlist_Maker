package com.example.myplaylistmaker.domain.models

import java.io.Serializable
import java.util.concurrent.TimeUnit

private const val SECONDS_IN_MINUTE = 60

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Serializable {
    // Не Parcelable потому что доменные модели не должны знать об Андроиде.
    val formattedTrackTime: String
        get() {
            val trackTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis)
            val seconds = trackTimeInSeconds % SECONDS_IN_MINUTE
            val minutes = trackTimeInSeconds / SECONDS_IN_MINUTE
            return "$minutes:$seconds"
        }
}
