package com.example.myplaylistmaker


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {

    var darkTheme = false

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    override fun onCreate() {
        super.onCreate()
        savedHistory = applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }
    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences():SharedPreferences { return savedHistory}
        var mediaHistoryList = ArrayList<MediaData>()
    }
}