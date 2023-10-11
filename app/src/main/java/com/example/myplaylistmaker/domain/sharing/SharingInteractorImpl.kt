package com.example.myplaylistmaker.domain.sharing

import com.example.myplaylistmaker.creator.Creator

class SharingInteractorImpl(
    private var externalNavigator: ExternalNavigator,
) :SharingInteractor {
    init {
        externalNavigator = Creator.provideExternalNavigator()
    }

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }

    private fun getShareAppLink(): String {

        return externalNavigator.getShareLink ()
    }
}