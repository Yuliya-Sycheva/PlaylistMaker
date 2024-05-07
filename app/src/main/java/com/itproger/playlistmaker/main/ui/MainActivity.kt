package com.itproger.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itproger.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            NavigatorToActivity.navigateToSearchActivity(this)
        }

        binding.library.setOnClickListener {
            NavigatorToActivity.navigateToLibraryActivity(this)
        }

        binding.settings.setOnClickListener {
            NavigatorToActivity.navigateToSettingsActivity(this)
        }

    }
}