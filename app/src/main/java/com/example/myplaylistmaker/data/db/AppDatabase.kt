package com.example.myplaylistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylistmaker.data.db.dao.TrackDao
import com.example.myplaylistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao(): TrackDao
}