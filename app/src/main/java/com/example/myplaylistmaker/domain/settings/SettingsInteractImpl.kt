package com.example.myplaylistmaker.domain.settings



class SettingsInteractImpl (private var themeSettings: ThemeSettings):SettingsInteractor {


    private var isDarkTheme = true

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