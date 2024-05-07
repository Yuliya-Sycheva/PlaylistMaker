package com.itproger.playlistmaker.settings.ui.view_model

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.itproger.playlistmaker.settings.creator.SettingsCreator
import com.itproger.playlistmaker.settings.domain.SettingsInteractor
import com.itproger.playlistmaker.sharing.creator.SharingCreator
import com.itproger.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,

) : ViewModel() {

    private var themeLiveData = MutableLiveData(true)

    init {
        themeLiveData.value = settingsInteractor.getThemeSettings().darkTheme
     //   applyTheme(themeLiveData.value ?: false)
    }

    fun getThemeLiveData(): LiveData<Boolean> = themeLiveData


    // нужно ли добавлять Application? Урок "Применяем MVVM", п. Domain-specific language
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application //////////
                    SettingsViewModel(
                        SharingCreator.provideSharingInteractor(application),
                        SettingsCreator.providesettingsInteractor(application)
                    )
                }
            }
    }

    fun clickShareApp() {
        sharingInteractor.shareApp()
    }

    fun clickOpenSupport() {
        sharingInteractor.openSupport()
    }

    fun clickOpenTerms() {
        sharingInteractor.openTerms()
    }

    fun clickSwitchTheme(isChecked: Boolean) {
        Log.d("TEST", "clickSwitchTheme")
        themeLiveData.value = isChecked                        //
        settingsInteractor.updateThemeSetting(isChecked)
              applyTheme(isChecked)
    }


    private fun applyTheme(darkThemeEnabled: Boolean) {
        Log.d("TEST", "applyTheme")
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}



//    companion object {
//        fun getViewModelFactory(
//            sharingInteractor: SharingInteractor,
//            settingsInteractor: SettingsInteractor
//        ): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return SettingsViewModel(
//                        SharingCreator.provideSharingInteractor(),
//                        SettingsCreator.providesettingsInteractor()
//                    ) as T
//                }
//            }
//    }