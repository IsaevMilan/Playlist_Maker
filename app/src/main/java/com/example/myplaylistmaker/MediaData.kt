package com.example.myplaylistmaker


data class MediaData(
   var trackName: String,
   var artistName: String,
   var trackTimeMillis: Int,
   var artworkUrl100: String,
   val mediaId: String)