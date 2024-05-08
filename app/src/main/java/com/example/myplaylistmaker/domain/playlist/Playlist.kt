package com.example.myplaylistmaker.domain.playlist

import android.os.Parcel
import android.os.Parcelable


data class Playlist (
    val playlistId: Int? = 0,
    val playlistName:String,
    val description:String?,
    val uri:String,
    var trackArray:List<Long?>,
    var arrayNumber: Int?
)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.createLongArray()?.toList() ?: emptyList(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(playlistId)
        parcel.writeString(playlistName)
        parcel.writeString(description)
        parcel.writeString(uri)
        parcel.writeLongArray(trackArray.mapNotNull { it?.toLong() }.toLongArray())
        parcel.writeValue(arrayNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}