package com.example.myplaylistmaker.ui.main_activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myplaylistmaker.databinding.ActivityMainBinding
import com.example.myplaylistmaker.ui.media_library_activities.MediaLibraryActivity
import com.example.myplaylistmaker.ui.search.activity.SearchActivity
import com.example.myplaylistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Log.d("MainActivityButton", "SearchButton setOnClickListener")
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }
        binding.button2.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaLibraryActivity::class.java
                )
            )
        }
        binding.button3.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }
}

