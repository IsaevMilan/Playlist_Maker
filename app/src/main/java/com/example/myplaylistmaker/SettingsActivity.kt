package com.example.myplaylistmaker
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButt = findViewById<ImageView>(R.id.arrowBack)

        backButt.setOnClickListener { val searchIntent = Intent (this, MainActivity::class.java )
            startActivity(searchIntent)

        }
    }
}