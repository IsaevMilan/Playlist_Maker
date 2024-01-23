package com.example.myplaylistmaker.data.converters

import android.util.Log
import com.example.myplaylistmaker.data.db.entity.NewPlaylistEntity
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NewPlaylistConverter {
    val gson = Gson()
    fun mapplaylistEntityToClass(item: NewPlaylistEntity): NewPlaylist {

        return NewPlaylist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.fromJson(item.trackList, object : TypeToken<List<Long>>() {}.type),
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: NewPlaylist): NewPlaylistEntity {
        val tracklist = item.trackArray.toString()
        Log.d("Запись в плейлист", "пишем в конвертер  $tracklist")
        return NewPlaylistEntity(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.toJson(item.trackArray),
            item.arrayNumber
        )
    }
}