package com.example.myplaylistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myplaylistmaker.data.db.entity.NewPlaylistEntity

@Dao
interface NewPlaylistDao {

    @Insert(entity= NewPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist (playlist: NewPlaylistEntity)

    @Delete(entity=NewPlaylistEntity::class)
    fun deletePlaylist (playlist: NewPlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun queryPlaylist () : List <NewPlaylistEntity>

    @Update(entity = NewPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: NewPlaylistEntity)
}