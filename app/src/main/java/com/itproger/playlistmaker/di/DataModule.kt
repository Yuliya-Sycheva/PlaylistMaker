package com.itproger.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.network.ITunesApi
import com.itproger.playlistmaker.search.data.network.RetrofitNetworkClient
import com.itproger.playlistmaker.search.data.preferences.SearchHistoryStorage
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import com.itproger.playlistmaker.utils.GeneralConstants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val dataModule = module {

    single<ITunesApi> {
        Log.d("TEST", "ITunesApi_Module")
        Retrofit.Builder()
            .baseUrl(GeneralConstants.iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java) // factory?
    }

    single <SharedPreferences> {   //добавила в <>
        Log.d("TEST", "getSharedPreferences_Module")
        androidContext().getSharedPreferences(
            GeneralConstants.SEARCH_HISTORY_PREFERENCES,
            // AppCompatActivity.MODE_PRIVATE
            Context.MODE_PRIVATE
        )
    }

    factory {
        Log.d("TEST", " Gson_Module")
        Gson()
    }

    single <SearchHistoryStorage> {  // поменяла на factory
        Log.d("TEST", "SharedPreferencesSearchHistoryStorage_Module")
        SharedPreferencesSearchHistoryStorage(sharedPreferences = get(), gson = get())  //
    }

    single<NetworkClient> {      //single?
        Log.d("TEST", "RetrofitNetworkClient_Module")
        RetrofitNetworkClient(api = get(), context = androidContext())
    }

    single<Executor> {
        Executors.newCachedThreadPool()
    }
}