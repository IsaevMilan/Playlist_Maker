package com.example.myplaylistmaker.data.local

import android.content.SharedPreferences
import com.google.gson.Gson


interface SearchHistoryStorage {

}

class SharedPreferencesSearchHistoryStorage(

    private val prefs: SharedPreferences,
    private val gson: Gson,
) : SearchHistoryStorage {

}