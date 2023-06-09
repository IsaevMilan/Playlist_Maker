package com.example.myplaylistmaker

data class MediaData(


   var trackName: String,
   var artistName: String,
   var trackTimeMillis: Int,
   var artworkUrl100: String,
   val trackId: String,
   val collectionName: String,
   val releaseDate: String,
   val primaryGenreName: String,
   val country: String,
   val previewUrl: String)
{
   override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val track = other as MediaData
      return trackId == track.trackId
   }

   override fun hashCode(): Int {
      return trackId.hashCode()
   }
}