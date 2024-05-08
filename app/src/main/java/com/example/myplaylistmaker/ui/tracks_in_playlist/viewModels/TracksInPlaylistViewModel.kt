package com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import com.example.myplaylistmaker.domain.tracks_in_playlist.TracksInPlaylistInteractor
import kotlinx.coroutines.launch

class TracksInPlaylistViewModel(private val tracksInPlaylistInteractor: TracksInPlaylistInteractor,
                                private val settingsInteractor: SettingsInteractor,
                                private val playlistInteractor: PlaylistInteractor,
                                private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isAppThemeDark()
    }

    val trackList : MutableLiveData <List <Track>> = MutableLiveData(emptyList())
    fun trackListLiveData(): LiveData<List <Track>> = trackList
    fun getTrackList (playlist: Playlist) {
        viewModelScope.launch {
            tracksInPlaylistInteractor.getTrackList(playlist).collect {
                    list -> trackList.postValue(list)
            }
        }
    }

    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }

    fun deleteTrack (track: Track, playlist: Playlist){
        playlist.trackArray = playlist.trackArray.filter { it != track.trackId }
        playlist.arrayNumber = playlist.arrayNumber?.minus(1)
        playlistInteractor.update(track, playlist)
    }

    val playlistTime: MutableLiveData <String> = MutableLiveData("")
    fun getPlaylistTime (playlist: Playlist) {
        viewModelScope.launch {
            tracksInPlaylistInteractor.timeCounting(playlist).collect{
                    readyTime -> playlistTime.postValue(readyTime)
            }
        }
    }

    val updatedPlaylist :MutableLiveData<Playlist> = MutableLiveData()
    fun getPlaylist (searchId: Int) {
        viewModelScope.launch {
            playlistInteractor.findPlaylist(searchId).collect{
                updatedPlaylist.postValue(it)
            }
        }
    }
    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }
}
