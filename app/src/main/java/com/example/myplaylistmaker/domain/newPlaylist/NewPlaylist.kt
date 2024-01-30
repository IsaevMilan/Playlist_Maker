package com.example.myplaylistmaker.domain.newPlaylist


data class NewPlaylist (
    val playlistId: Int? = 0,
    val playlistName:String,
    val description:String?,
    val uri:String,
    var trackArray:List<Long?>,
    var arrayNumber:Int?,
)