package com.itproger.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.dto.Response
import com.itproger.playlistmaker.search.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {

        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TrackRequest) {
            return Response().apply { resultCode = 400 }
        }

        val response = iTunesService.search(dto.text).execute()

        val body = response.body()

        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }

//        if (dto is TrackRequest) {
//            val resp = iTunesService.search(dto.text).execute()
//
//            val body = resp.body() ?: Response()
//
//            return body.apply { resultCode = resp.code() }
//        } else {
//            return Response().apply { resultCode = 400 }
//        }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}