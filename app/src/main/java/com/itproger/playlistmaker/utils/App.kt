package com.itproger.playlistmaker.utils

import android.app.Application
import android.os.Parcelable.Creator
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewmodel.CreationExtras
import com.itproger.playlistmaker.settings.creator.SettingsCreator
import com.itproger.playlistmaker.settings.domain.SettingsInteractor
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel


class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = SettingsCreator.providesettingsInteractor(this)
        darkTheme = settingsInteractor.getThemeSettings().darkTheme
     //   applyTheme(darkTheme)

    }

//    private fun applyTheme(darkThemeEnabled: Boolean) {
//        AppCompatDelegate.setDefaultNightMode(
//            if (darkThemeEnabled) {
//                AppCompatDelegate.MODE_NIGHT_YES
//            } else {
//                AppCompatDelegate.MODE_NIGHT_NO
//            }
//        )
//    }
}