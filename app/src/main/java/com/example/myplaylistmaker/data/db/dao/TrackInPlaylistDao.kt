package com.example.myplaylistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.myplaylistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.domain.search.models.Track

@Dao
interface TrackInPlaylistDao {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: Track)
}