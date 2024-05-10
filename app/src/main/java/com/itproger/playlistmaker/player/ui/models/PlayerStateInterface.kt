package com.itproger.playlistmaker.player.ui.models

sealed interface PlayerStateInterface {
    object Prepare : PlayerStateInterface
    object Play : PlayerStateInterface
    object Pause : PlayerStateInterface

    data class UpdatePlayingTime(val time: String) : PlayerStateInterface
}