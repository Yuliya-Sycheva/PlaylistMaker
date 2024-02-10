package com.itproger.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "playlist_maker_theme_preferences"
const val NIGHT_THEME_KEY = "night_theme"

class App : Application() {

     private val sharedPrefs by lazy {
         getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
     }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        darkTheme = getThemeFromSharedPreferences()
        switchTheme(darkTheme)
    }

    private fun getThemeFromSharedPreferences(): Boolean {
        return sharedPrefs.getBoolean(NIGHT_THEME_KEY, false)
    }

    private fun saveThemeToSharedPreferences() {
        sharedPrefs.edit()
            .putBoolean(NIGHT_THEME_KEY, darkTheme)
            .apply()
    }

    private fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        applyTheme(darkThemeEnabled)
        saveThemeToSharedPreferences()
    }
}