package com.itproger.playlistmaker.sharing.domain

interface ExternalNavigator {

    fun shareLink()
    fun openLink()
    fun openEmail()
}