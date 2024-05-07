package com.itproger.playlistmaker.main.ui

import android.content.Context
import android.content.Intent
import com.itproger.playlistmaker.library.ui.LibraryActivity
import com.itproger.playlistmaker.search.ui.SearchActivity
import com.itproger.playlistmaker.settings.ui.activity.SettingsActivity

object NavigatorToActivity {
    fun navigateToSearchActivity(context: Context) {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    fun navigateToLibraryActivity(context: Context) {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        context.startActivity(libraryIntent)
    }

    fun navigateToSettingsActivity(context: Context) {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }
}