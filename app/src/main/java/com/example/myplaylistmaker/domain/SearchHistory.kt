package com.example.myplaylistmaker.domain

import android.content.Context
import android.widget.Toast
import com.example.myplaylistmaker.presentation.ui.activities.SEARCH_SHARED_PREFS_KEY
import com.example.myplaylistmaker.app.App
import com.example.myplaylistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    private val gson = Gson()
    var counter = 0
    val trackHistoryList = ArrayList<Track>()

    init {
        trackHistoryList.addAll(readFromPrefs())
    }

    fun editArray(newHistoryTrack: Track) {
        if (trackHistoryList.contains(newHistoryTrack)) {
            trackHistoryList.remove(newHistoryTrack)
            trackHistoryList.add(0, newHistoryTrack)
        } else {
            if (trackHistoryList.size < 10) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    private fun saveHistory() {
        savedHistory.edit()
            .clear()
            .putString(SEARCH_SHARED_PREFS_KEY, gson.toJson(trackHistoryList))
            .apply()
        counter = trackHistoryList.size
    }

    fun toaster(context: Context, text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    private fun readFromPrefs(): List<Track> {
        val json = savedHistory.getString(SEARCH_SHARED_PREFS_KEY, "")
        if (json.isNullOrBlank()) {
            return emptyList()
        }
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }
}