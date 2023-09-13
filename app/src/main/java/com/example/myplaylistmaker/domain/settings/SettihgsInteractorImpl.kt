package com.example.myplaylistmaker.domain.settings

import com.example.myplaylistmaker.creator.Creator


class SettingsInteractImpl (private var themeSettings: ThemeSettings):SettingsInteractor {
    init {
        themeSettings= Creator.provideThemeSettings()
    }
    var isDarkTheme = true

    //получение информации о включении темной темы
    override fun isAppThemeDark(): Boolean {
        isDarkTheme=themeSettings.lookAtTheme ()
        return isDarkTheme
    }

    //функция смены темы:светлая/темная
    override fun changeThemeSettings(): Boolean {
        isDarkTheme=themeSettings.appThemeSwitch ()
        return isDarkTheme
    }
}