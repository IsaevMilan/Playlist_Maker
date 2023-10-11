package com.example.myplaylistmaker.domain.sharing

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)

    fun openLink ()

    fun openEmail ()

    fun getShareLink ():String

}