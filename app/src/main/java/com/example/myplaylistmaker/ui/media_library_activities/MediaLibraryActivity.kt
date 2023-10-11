package com.example.myplaylistmaker.ui.media_library_activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylistmaker.R


class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val backButt = findViewById<ImageView>(R.id.arrowBack2)
        backButt.setOnClickListener {
            finish()
        }
    }
}