package com.itproger.playlistmaker.library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity(), LibrarySelectPage {

    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.inFavoriteTracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun navigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}