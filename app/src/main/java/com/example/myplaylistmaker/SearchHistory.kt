package com.example.myplaylistmaker


import android.content.SharedPreferences
import com.google.gson.Gson


class SearchHistory (val sharedPreferences: SharedPreferences) {


    val searchedTrackList = mutableListOf<MediaData>()

    init {
        val searchedTrack = sharedPreferences.getString(TRACKS_LIST_KEY, "") ?: ""
        if (searchedTrack.isNotEmpty()) {
            searchedTrackList.addAll(createTrackListFromJson(searchedTrack))
        }
    }

    fun addNewTrack(track: MediaData) {

        if (searchedTrackList.contains(track)) searchedTrackList.remove(track)

        searchedTrackList.add(0, track)

        if (searchedTrackList.size == 11) searchedTrackList.removeAt(10)

        sharedPreferences.edit()
            .putString(TRACKS_LIST_KEY, createJsonFromTrackList(searchedTrackList.toTypedArray()))
            .apply()
    }

    fun clearHistory() {
        searchedTrackList.clear()
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    // метод дессириализует массив объектов Fact (в Shared Preference они хранятся в виде json строки)
    private fun createTrackListFromJson(json: String?): Array<MediaData> {
        return Gson().fromJson(json, Array<MediaData>::class.java)
    }

    // метод серриализует массив объектов Fact (переводит в формат json)
    private fun createJsonFromTrackList(facts: Array<MediaData>): String {
        return Gson().toJson(facts)
    }

}