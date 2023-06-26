package com.example.myplaylistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MediaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val backButt = findViewById<ImageView>(R.id.arrowBack2)

        backButt.setOnClickListener {
            onBackPressed()
        }


    }
}


     
