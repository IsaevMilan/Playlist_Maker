package com.example.myplaylistmaker.ui.tracks_in_playlist.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentTracksInPlaylistBinding
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels.TracksInPlaylistViewModel
import com.example.myplaylistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksInPlaylistFragment : Fragment() {
    private val tracksInPlaylistViewModel by viewModel<TracksInPlaylistViewModel>()
    private lateinit var binding: FragmentTracksInPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksInPlaylistBinding.inflate(inflater, container, false)
        //Нижний навигатор
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = GONE

        //отработка на кнопку назад
        binding.backArrow4.setOnClickListener {
            onBackClick()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>(/* key = */ "playlist")
        if (playlist != null) {
            playlist.playlistId?.let { tracksInPlaylistViewModel.getPlaylist(it) }
        }
        val checkedPlaylist = playlist?.let { drawPlaylist(it) }
        if (checkedPlaylist != null) {
            drawCover(checkedPlaylist)
            drawMenuBottomSheet()
        }
    }

    private fun clickAdapting(item: Track) {
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        val navController = findNavController()
        navController.navigate(R.id.action_tracksInPlaylistFragment_to_playerFragment, bundle)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackListMaker() {
        tracksInPlaylistViewModel.trackList.observe(viewLifecycleOwner) { trackList ->
            if (trackList.isNullOrEmpty()) {
                binding.emptyList.visibility = VISIBLE
                binding.trackInPlaylistRecycler.visibility = GONE
            } else {
                trackAdapter.setItems(trackList)
                trackAdapter.notifyDataSetChanged()
                binding.emptyList.visibility = GONE
                binding.trackInPlaylistRecycler.visibility = VISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTrackByClick(item: Track, playlist: Playlist) {
        tracksInPlaylistViewModel.deleteTrack(item, playlist)
        tracksInPlaylistViewModel.getTrackList(playlist)
        trackListMaker()
        drawPlaylist(playlist)
    }

    private fun deletePlaylist(item: Playlist) {
        tracksInPlaylistViewModel.deletePlaylist(item)
        val navController = findNavController()
        navController.navigate(R.id.action_tracksInPlaylistFragment_to_playerFragment)
    }

    private fun suggestPlaylistDeleting(playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = tracksInPlaylistViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deletePlaylist(playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun suggestTrackDeleting(track: Track, playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = tracksInPlaylistViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Вы уверены, что хотите удалить трек из плейлиста??")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deleteTrackByClick(track, playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun sharePlaylist(playlist: Playlist) {
        val nameOfPlaylist = playlist.playlistName
        val desriptionOfPlaylist = playlist.description
        val trackNumber = playlist.arrayNumber
        if (trackNumber == 0) {
            val message = "В данном плейлисте нет списка треков, которым можно поделиться."
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            return
        }
        var trackInfo = "$nameOfPlaylist \n $desriptionOfPlaylist \n $trackNumber треков \n"
        val trackList: List<Track> = tracksInPlaylistViewModel.trackList.value!!
        var i = 0
        trackList.forEach { track ->
            i += 1
            val name = track.trackName
            val duration = track.trackTimeMillis
            trackInfo = "$trackInfo $i. $name  - ($duration) \n"
        }

        val intentSend = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trackInfo)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        requireContext().startActivity(intentSend, null)
    }

    private fun onBackClick() {
        val fragmentmanager = requireActivity().supportFragmentManager
        bottomNavigator.visibility = VISIBLE
        fragmentmanager.popBackStack()
    }

    private fun playlistTime(playlist: Playlist) {
        tracksInPlaylistViewModel.getPlaylistTime(playlist)
        tracksInPlaylistViewModel.playlistTime.observe(viewLifecycleOwner) { playlistTime ->
            binding.playlistTime.text = playlistTime
        }
    }

    private fun drawPlaylist(playlist: Playlist): Playlist {
        var checkedPlaylist = playlist
        tracksInPlaylistViewModel.updatedPlaylist.observe(viewLifecycleOwner) { updatedPlaylist ->
            checkedPlaylist = updatedPlaylist
            binding.PlaylistName.text = checkedPlaylist.playlistName
            binding.descriptionOfPlaylist.text = checkedPlaylist.description ?: ""
            playlistTime(checkedPlaylist)

            //сколько треков в плейлисте
            val trackCounter = (checkedPlaylist.arrayNumber).toString()
            val text = when {
                trackCounter.toInt() % 10 == 1 && trackCounter.toInt() % 100 != 11 -> " трек"
                trackCounter.toInt() % 10 == 2 && trackCounter.toInt() % 100 != 12 -> " трека"
                trackCounter.toInt() % 10 == 3 && trackCounter.toInt() % 100 != 13 -> " трека"
                trackCounter.toInt() % 10 == 4 && trackCounter.toInt() % 100 != 14 -> " трека"
                else -> " треков"
            }
            binding.trackNumber.text = "$trackCounter $text"
            drawCover(checkedPlaylist)
            drawPlaylistDataBottomSheet(checkedPlaylist)
        }
        return checkedPlaylist
    }

    private fun drawCover(item: Playlist) {
        val baseWidth = 312
        val baseHeight = 312
        val getImage = item.uri
        if (getImage != "null") {
            Log.d("картинка", getImage)
            binding.playlistPlaceHolder.visibility = GONE
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .override(baseWidth, baseHeight)
                .into(binding.playlistCover)
        } else {
            binding.playlistPlaceHolder.visibility = VISIBLE
        }
    }

    private fun drawPlaylistDataBottomSheet(playlist: Playlist) {
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.trackInPlaylistContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        //список треков в плейлисте
        trackAdapter = TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = {
                suggestTrackDeleting(it, playlist)
            })
        tracksInPlaylistViewModel.getTrackList(playlist)
        trackListMaker()
        binding.trackInPlaylistRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.trackInPlaylistRecycler.adapter = trackAdapter

        //кнопки

        binding.share.setOnClickListener {
            sharePlaylist(playlist)
        }
        binding.shareText.setOnClickListener {
            sharePlaylist(playlist)
        }
        binding.editInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            val navController = findNavController()
            navController.navigate(R.id.action_tracksInPlaylistFragment_to_playListEditor, bundle)

        }
        binding.deletePlaylist.setOnClickListener {
            suggestPlaylistDeleting(playlist)
        }
    }

    private fun drawMenuBottomSheet() {
        val menuBottomSheetContainer = binding.editMenu
        val overlay = binding.overlay
        val menuBottomSheetBehavior = BottomSheetBehavior
            .from(menuBottomSheetContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        menuBottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                overlay.visibility = GONE
                            }

                            else -> {
                                overlay.visibility = VISIBLE
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )

        binding.editMenuButton.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
