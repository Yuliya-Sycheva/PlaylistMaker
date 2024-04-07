package com.itproger.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.itproger.playlistmaker.utils.App
import com.itproger.playlistmaker.databinding.ActivitySettingsBinding
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModelSettings: SettingsViewModel //1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSettings = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]  //2

        //Текущая тема:
        viewModelSettings.getThemeLiveData().observe(this) { isChecked ->
            // (applicationContext as App).darkTheme /////////////////////
            binding.themeSwitcher.isChecked = isChecked
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.termsOfViews.setOnClickListener {
            viewModelSettings.clickOpenTerms()
        }

        binding.support.setOnClickListener {
            viewModelSettings.clickOpenSupport()
        }

        binding.share.setOnClickListener {
            viewModelSettings.clickShareApp()
        }

        //      binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme    /////////////////убрать?? присваиваю значение темы из App

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            viewModelSettings.clickSwitchTheme(isChecked)
        }

//        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
//            (applicationContext as App).switchTheme(isChecked)
//        }
    }
}