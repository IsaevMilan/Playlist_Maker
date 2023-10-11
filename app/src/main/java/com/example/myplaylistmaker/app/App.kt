package com.example.myplaylistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.myplaylistmaker.creator.Creator


class App : Application() {
    private var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        //выставляем тему экрана
        val settingsInteractor = Creator.provideSettingsIneractor()
        appTheme = settingsInteractor.isAppThemeDark()
        makeTheme(appTheme)
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
    companion object {
        lateinit var instance: App
            private set
    }
}