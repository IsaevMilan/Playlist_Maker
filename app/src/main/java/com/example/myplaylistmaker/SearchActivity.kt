package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backSbutton = findViewById<Button>(R.id.backSbutton)

        backSbutton.setOnClickListener { val settingsIntent = Intent (this, MainActivity::class.java )
            startActivity(settingsIntent)

        }
    }
}