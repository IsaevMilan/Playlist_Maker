package com.example.myplaylistmaker.domain.player

interface PlayerStateListener {

  fun onStateChanged (state: PlayerState)
}