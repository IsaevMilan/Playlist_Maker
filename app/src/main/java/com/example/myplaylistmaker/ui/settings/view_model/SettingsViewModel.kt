package com.example.myplaylistmaker.ui.settings.view_model

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.app.App
import com.example.myplaylistmaker.creator.Creator
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import com.example.myplaylistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {
    init {
        sharingInteractor = Creator.provideSharingIneractor()
        settingsInteractor = Creator.provideSettingsIneractor()
    }

    //нажатие на кнопку назад
    private var onBackLiveData = MutableLiveData(false)
    fun onBackClick() {
        onBackLiveData.value = true
    }

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return SettingsViewModel(
                        Creator.provideSharingIneractor(),
                        Creator.provideSettingsIneractor()
                    ) as T
                }
            }
    }
}