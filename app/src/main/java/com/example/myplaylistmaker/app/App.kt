package com.example.myplaylistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.myplaylistmaker.di.mediaLibraryModule.mediaLibraryModule
import com.example.myplaylistmaker.di.player_module.PlayerModule
import com.example.myplaylistmaker.di.search_module.InteractorModule
import com.example.myplaylistmaker.di.search_module.RepositoryModule
import com.example.myplaylistmaker.di.search_module.SettingsModule
import com.example.myplaylistmaker.di.search_module.ViewModelModule
import com.example.myplaylistmaker.di.search_module.dataModule
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin


class App : Application() , KoinComponent {

    private var appTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin{

            androidContext(this@App)
            modules(
                dataModule,
                InteractorModule,
                RepositoryModule,
                PlayerModule,
                SettingsModule,
                ViewModelModule,
                mediaLibraryModule
            )
        }

        val settingsInteractor = getKoin().get<SettingsInteractor>()

        //выставляем тему экрана
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