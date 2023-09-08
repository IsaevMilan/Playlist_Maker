package com.example.myplaylistmaker.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myplaylistmaker.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        val settingsButton = findViewById<Button>(R.id.button3)
        val mediaButton = findViewById<Button>(R.id.button2)
        val searchButton = findViewById<Button>(R.id.button)

        settingsButton.setOnClickListener { val settingsIntent = Intent (this, SettingsActivity::class.java )
            startActivity(settingsIntent)

        }
        mediaButton.setOnClickListener { val settingsIntent = Intent (this, MediaActivity::class.java )
            startActivity(settingsIntent)

        }
        searchButton.setOnClickListener { val settingsIntent = Intent (this, SearchActivity::class.java )
            startActivity(settingsIntent)

        }
    }

}
