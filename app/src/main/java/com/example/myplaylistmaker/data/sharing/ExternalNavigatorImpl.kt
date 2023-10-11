package com.example.myplaylistmaker.data.sharing

import android.content.Intent
import android.net.Uri
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.app.App
import com.example.myplaylistmaker.domain.sharing.ExternalNavigator

class ExternalNavigatorImpl (private val application: App): ExternalNavigator {



    override fun shareLink(shareAppLink: String) {

        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.SHARE))
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }

    override fun openLink() {
        val intentAgreement3 =
            Intent(Intent.ACTION_VIEW, Uri.parse(application.getString(R.string.yaAgreementUrl)))
        intentAgreement3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentAgreement3)
    }

    override fun openEmail() {
        val intentSendTo2 = Intent(Intent.ACTION_SENDTO)
        intentSendTo2.data = Uri.parse("mailto:")
        val email = application.getString(R.string.myMail)
        intentSendTo2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intentSendTo2.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.support))
        intentSendTo2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSendTo2)
    }
    override fun getShareLink ():String {
        return "https://practicum.yandex.ru/profile/android-developer"
    }

}