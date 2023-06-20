package com.example.myplaylistmaker
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButt = findViewById<ImageView>(R.id.arrowBack)

        backButt.setOnClickListener { onBackPressed()
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)

        }

        val frameLayoutShare = findViewById<FrameLayout>(R.id.share)
        val devUrlString = getString(R.string.yaDeveloperUrl)

        frameLayoutShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, devUrlString)
                type = "text/plain"
            }



            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val frameLayoutSupport = findViewById<FrameLayout>(R.id.support)
        frameLayoutSupport.setOnClickListener {

            val myMailString = getString(R.string.myMail)
            val subject = getString(R.string.mailSubject)
            val message = getString(R.string.mailText)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(myMailString))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
        val frameLayoutAgreement = findViewById<FrameLayout>(R.id.agreement)

        frameLayoutAgreement.setOnClickListener {

            val yaAgreement = getString(R.string.yaAgreementUrl)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(yaAgreement))
            startActivity(browserIntent)
        }
    }
}