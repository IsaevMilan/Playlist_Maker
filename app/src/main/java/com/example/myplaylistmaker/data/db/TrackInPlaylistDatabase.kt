package com.example.myplaylistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylistmaker.data.db.dao.TrackInPlaylistDao
import com.example.myplaylistmaker.data.db.entity.TrackInPlaylistEntity

@Database(version = 1, entities = [TrackInPlaylistEntity::class])
abstract class TrackInPlaylistDatabase : RoomDatabase() {
    abstract fun tracklistDao(): TrackInPlaylistDao
}