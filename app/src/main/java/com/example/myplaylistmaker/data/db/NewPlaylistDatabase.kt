package com.example.myplaylistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylistmaker.data.db.dao.NewPlaylistDao
import com.example.myplaylistmaker.data.db.entity.NewPlaylistEntity

@Database(version = 1, entities = [NewPlaylistEntity::class])
abstract class NewPlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): NewPlaylistDao
}