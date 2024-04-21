package com.example.myplaylistmaker.domain.playlist


data class Playlist (
    val playlistId: Int? = 0,
    val playlistName:String,
    val description:String?,
    val uri:String,
    var trackArray:List<Long?>,
    var arrayNumber: Int?
) {

}