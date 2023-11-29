package com.example.myplaylistmaker.ui.settings.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylistmaker.databinding.FragmentSettingsBinding
import com.example.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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