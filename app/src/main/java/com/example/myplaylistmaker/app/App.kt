package com.example.myplaylistmaker.app


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myplaylistmaker.domain.models.Track
import com.example.myplaylistmaker.presentation.ui.activities.SEARCH_SHARED_PREFS_KEY


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences(): SharedPreferences {
            return savedHistory
        }

        var trackHistoryList = ArrayList<Track>()
    }
}