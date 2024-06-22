package com.itproger.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import com.google.gson.Gson
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.network.ITunesApi
import com.itproger.playlistmaker.search.data.network.RetrofitNetworkClient
import com.itproger.playlistmaker.search.data.preferences.SearchHistoryStorage
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val SEARCH_HISTORY_PREFERENCES = "playlist_maker_search_history_preferences"
private const val iTunesBaseUrl = "https://itunes.apple.com"
private const val THEME_PREFERENCES = "playlist_maker_theme_preferences"

val dataModule = module {

    single<ITunesApi> {
        Log.d("TEST", "ITunesApi_Module")
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<SharedPreferences> {   //добавила в <>
        Log.d("TEST", "getSharedPreferences_Module")
        androidContext().getSharedPreferences(
            SEARCH_HISTORY_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single {
        Log.d("TEST", " Gson_Module")
        Gson()
    }

    single<SearchHistoryStorage> {
        Log.d("TEST", "SharedPreferencesSearchHistoryStorage_Module")
        SharedPreferencesSearchHistoryStorage(sharedPreferences = get(), gson = get())  //
    }

    single<NetworkClient> {
        Log.d("TEST", "RetrofitNetworkClient_Module")
        RetrofitNetworkClient(api = get(), context = androidContext())
    }

    single<Executor> {
        Executors.newCachedThreadPool()
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single {
        Log.d("TEST", "getSharedPreferences_Settings_Module")
        androidContext().getSharedPreferences(
            THEME_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}