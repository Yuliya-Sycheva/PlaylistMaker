package com.itproger.playlistmaker.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.itproger.playlistmaker.settings.creator.SettingsCreator
import com.itproger.playlistmaker.settings.domain.SettingsInteractor


class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = SettingsCreator.providesettingsInteractor(this)
        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        applyTheme(darkTheme)

    }

    fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}