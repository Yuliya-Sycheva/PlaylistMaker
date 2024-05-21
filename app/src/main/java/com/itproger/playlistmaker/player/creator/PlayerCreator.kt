package com.itproger.playlistmaker.player.creator

import android.content.Context
import android.media.MediaPlayer
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.domain.interactor.impl.PlayerInteractorImpl
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository

//object PlayerCreator {
//    private fun getPlayerRepository(): PlayerRepository {
//        return PlayerRepositoryImpl()
//    }
//
//    fun providePlayerInteractor(): PlayerInteractor {
//        return PlayerInteractorImpl(getPlayerRepository())
//    }
//}