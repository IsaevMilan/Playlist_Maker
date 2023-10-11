package com.example.myplaylistmaker.domain.settings

interface SettingsInteractor {
    fun isAppThemeDark():Boolean

    fun changeThemeSettings():Boolean
}