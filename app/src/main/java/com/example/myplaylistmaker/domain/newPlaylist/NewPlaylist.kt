package com.example.myplaylistmaker.domain.newPlaylist

import android.os.Parcelable



data class NewPlaylist (
    val playlistId: Int? = 0,
    val playlistName:String,
    val description:String?,
    val uri:String,
    var trackArray:List<Long?>,
    var arrayNumber:Int?,
) : Parcelable