package com.example.myplaylistmaker
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButt = findViewById<ImageView>(R.id.arrowBack)

        backButt.setOnClickListener { onBackPressed()
        }

        val frameLayoutShare = findViewById<FrameLayout>(R.id.share)

        frameLayoutShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val frameLayoutSupport = findViewById<FrameLayout>(R.id.support)
        frameLayoutSupport.setOnClickListener {

            val subject = "Спасибо разработчикам и разработчицам приложения Playlist Maker!"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("YAmilanisaev@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
        val frameLayoutAgreement = findViewById<FrameLayout>(R.id.agreement)

        frameLayoutAgreement.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(browserIntent)
        }
    }
}