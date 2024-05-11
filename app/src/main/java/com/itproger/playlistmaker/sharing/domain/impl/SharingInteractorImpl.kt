package com.itproger.playlistmaker.sharing.domain.impl

import com.itproger.playlistmaker.sharing.domain.ExternalNavigator
import com.itproger.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(

    private val externalNavigator: ExternalNavigator,

    ) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }
}