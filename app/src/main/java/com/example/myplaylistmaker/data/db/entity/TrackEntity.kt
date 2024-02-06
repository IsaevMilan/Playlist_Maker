package com.example.myplaylistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long?,
    val addTime:Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val isFavorite: Boolean = false
)
