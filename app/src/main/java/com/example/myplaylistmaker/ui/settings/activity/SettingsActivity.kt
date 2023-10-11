package com.example.myplaylistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.databinding.ActivitySettingsBinding
import com.example.myplaylistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем viewmodel
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        //кнопка назад
        binding.arrowBack.setOnClickListener {
            settingsViewModel.onBackClick()
        }
        settingsViewModel.getOnBackLiveData()
            .observe(this) { onBackLiveData -> onBackClick(onBackLiveData) }


        // обновление темы
        binding.themeSwitcher.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        binding.themeSwitcher.setOnClickListener {
            settingsViewModel.themeSwitch()
            binding.themeSwitcher.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        }

        //Поделиться
        binding.share.setOnClickListener {
            settingsViewModel.shareApp()
        }

        //Написать в поддержку
        binding.support.setOnClickListener {
            settingsViewModel.writeSupport()
        }

        //share
        binding.agreement.setOnClickListener {
            settingsViewModel.readAgreement ()
        }
    }

    private fun onBackClick(back: Boolean) {
        if (back) {
            finish()
        }
    }
}