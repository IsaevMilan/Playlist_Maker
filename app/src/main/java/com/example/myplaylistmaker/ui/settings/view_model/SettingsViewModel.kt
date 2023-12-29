package com.example.myplaylistmaker.ui.settings.view_model

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import com.example.myplaylistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {


    //нажатие на кнопку назад
    private var onBackLiveData = MutableLiveData(false)


    fun getOnBackLiveData(): LiveData<Boolean> = onBackLiveData


    //Сохраняем тему в LiveData

    private var themeLiveData = MutableLiveData(settingsInteractor.isAppThemeDark())
    fun getThemeLiveData(): LiveData<Boolean> {
        val getting = if (themeLiveData.value!!) "day" else "night"
        Log.d("Тема", "ViewModel get $getting")
        return themeLiveData
    }

    fun themeSwitch() {
        themeLiveData.value = settingsInteractor.changeThemeSettings()
        val getting = if (themeLiveData.value!!) "day" else "night"
        Log.d("Тема", "ViewModel switch $getting")
        makeTheme(themeLiveData.value!!)
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    //делимся приложением
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    //пишем в поддержку
    fun writeSupport() {
        sharingInteractor.openSupport()
    }


    //читаем соглашение
    fun readAgreement() {
        sharingInteractor.openTerms()
    }



}