package com.itproger.playlistmaker.di

import android.content.Context
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

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(GeneralConstants.iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java) // factory?
    }

    single {
        androidContext().getSharedPreferences(
            GeneralConstants.SEARCH_HISTORY_PREFERENCES,
            // AppCompatActivity.MODE_PRIVATE
            Context.MODE_PRIVATE
        )
    }

    factory {
        Gson()
    }

    single<SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(sharedPreferences = get(), gson = get())  //
    }

    single<NetworkClient> {      //single?
        RetrofitNetworkClient(api = get(), context = androidContext())
    }
}