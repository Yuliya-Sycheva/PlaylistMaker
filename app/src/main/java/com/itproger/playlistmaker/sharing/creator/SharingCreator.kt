package com.itproger.playlistmaker.sharing.creator

import android.content.Context
import com.itproger.playlistmaker.sharing.domain.ExternalNavigator
import com.itproger.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.itproger.playlistmaker.sharing.domain.SharingInteractor
import com.itproger.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object SharingCreator {

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}