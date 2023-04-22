package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MediaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val backMbutton = findViewById<Button>(R.id.backMbutton)

        backMbutton.setOnClickListener { val settingsIntent = Intent (this, MainActivity::class.java )
            startActivity(settingsIntent)

        }
    }
}