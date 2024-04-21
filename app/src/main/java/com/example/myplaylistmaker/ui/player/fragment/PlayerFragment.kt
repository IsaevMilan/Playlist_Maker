package com.example.myplaylistmaker.ui.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.mediaLibrary.adapters.PlayerBottomSheetAdapter
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityMediaPlayerBinding
    private var url = ""
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var playlistAdapter: PlayerBottomSheetAdapter
    private var track: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = arguments?.getParcelable("track")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMediaPlayerBinding.inflate(inflater)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

        //нажатие на кнопку "новый плейлист"
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            Log.d("PlayerFragment", "onViewCreated() вызван")

            binding.backArrow4.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.favorites.setImageResource(if (track?.isFavorite == true) R.drawable.like_button else R.drawable.button_heart)
            binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
            binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
            binding.time.text = track?.trackTimeMillis ?: "00:00"
            binding.album.text = track?.collectionName ?: "Unknown Album"
            binding.year.text = (track?.releaseDate ?: "Year").take(4)
            binding.genre.text = track?.primaryGenreName ?: "Unknown Genre"
            binding.country.text = track?.country ?: "Unknown Country"
            val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
                "100x100bb.jpg",
                "512x512bb.jpg"
            )
            val radius = 8
            if (getImage != "Unknown Cover") {
                getImage.replace("100x100bb.jpg", "512x512bb.jpg")
                Glide.with(this)
                    .load(getImage)
                    .placeholder(R.drawable.placeholdermedia)
                    .transform(RoundedCorners(radius))
                    .into(binding.trackCover)
            }
            url = track?.previewUrl ?: return

            playerViewModel.createPlayer(url)

            binding.playButton.setOnClickListener {
                if (playerViewModel.stateLiveData().value == PlayerState.STATE_PLAYING)
                    playerViewModel.pause() else playerViewModel.play()
            }

            playerStateDrawer()

            playerViewModel.getTimeFromInteractor().observe(viewLifecycleOwner) { timer ->
                binding.trackTimer.text = timer
                Log.d("время в активити", timer)
            }

            //нажатие на кнопку нравится
            binding.favorites.setOnClickListener {
                playerViewModel.onFavoriteClicked(track)
            }

            playerViewModel.isFavoriteLiveData()
                .observe(viewLifecycleOwner) { isFavorite ->
                    val imageResId = if (isFavorite) R.drawable.like_button else R.drawable.button_heart
                    binding.favorites.setImageResource(imageResId)
                }


//             playerViewModel.clickFavourites(track)
//               .observe(viewLifecycleOwner) { favourtitesIndicator ->
//                   if (favourtitesIndicator) {
//                       binding.favorites.setImageResource(R.drawable.like_button)
//                   } else binding.favorites.setImageResource(
//                       R.drawable.buttonhert
//                   )
//               }



            //BottomSheet

            val bottomSheetContainer = binding.standardBottomSheet
            val standardBottomSheet = binding.standardBottomSheet
            val bottomSheetBehavior = BottomSheetBehavior
                .from(bottomSheetContainer)
                .apply {
                    state = STATE_HIDDEN
                }
            bottomSheetBehavior
                .addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            when (newState) {
                                STATE_HIDDEN -> {
                                    standardBottomSheet.visibility = View.GONE
                                }

                                else -> {
                                    standardBottomSheet.visibility = VISIBLE
                                }
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    }
                )

            //нажатие на кнопку "добавить в плейлист"
            binding.playlistAddButton.setOnClickListener {
                bottomSheetBehavior.state = STATE_COLLAPSED
                binding.standardBottomSheet.visibility = VISIBLE
            }

            //список плейлистов
            if (!playerViewModel.myPlaylist.value.isNullOrEmpty()) {
                playlistAdapter = playerViewModel.myPlaylist.value?.let { it ->
                    PlayerBottomSheetAdapter(it) {
                        playlistClickAdapting(track, it)
                        bottomSheetBehavior.state = STATE_HIDDEN


                    }
                }!!
            } else {
                playlistAdapter = PlayerBottomSheetAdapter(emptyList()) {}
            }
            val recyclerView = binding.playlistRecycler
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = playlistAdapter

            playerViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
                if (playlistList.isNullOrEmpty()) return@observe
                binding.playlistRecycler.adapter = PlayerBottomSheetAdapter(playlistList) {
                    playlistClickAdapting(track, it)
                    bottomSheetBehavior.state = STATE_HIDDEN
                    Log.d("Запись в плейлист", "click!")
                }
            }
        }

        override fun onPause() {
            super.onPause()
            playerViewModel.pause()
        }

        override fun onDestroy() {
            super.onDestroy()
            playerViewModel.destroy()
        }

        private fun preparePlayer() {
            binding.playButton.isEnabled = true
            binding.playButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.GONE
        }

        fun playerStateDrawer() {
            playerViewModel.stateLiveData().observe(viewLifecycleOwner) {
                when (playerViewModel.stateLiveData().value) {
                    PlayerState.STATE_DEFAULT -> {
                        binding.playButton.setImageResource(R.drawable.buttonplay)

                    }

                    PlayerState.STATE_PREPARED -> {
                        preparePlayer()
                        binding.playButton.setImageResource(R.drawable.buttonplay)

                    }

                    PlayerState.STATE_PLAYING -> {
                        binding.playButton.setImageResource(R.drawable.pause_button)

                    }

                    PlayerState.STATE_PAUSED -> {
                        binding.playButton.setImageResource(R.drawable.buttonplay)

                    }

                    else -> {
                    }
                }
            }
        }

    private fun closer() {
        val fragmentmanager = requireActivity().supportFragmentManager
        bottomNavigator.visibility = VISIBLE
        fragmentmanager.popBackStack()
    }

    private fun playlistClickAdapting(track: Track?, playlist: Playlist) {
        track ?: return
        var trackIsAdded = false
        playerViewModel.addTrack(track, playlist)
        lifecycleScope.launch {
            delay(300)
            playerViewModel.playlistAdding.observe(viewLifecycleOwner) { playlistAdding ->

                val playlistName = playlist.playlistName
                if (!trackIsAdded) {
                    if (playlistAdding) {

                        Log.d("Запись в плейлист", "Уже есть ")
                        val toastMessage = "Трек уже добавлен в плейлист $playlistName"


                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    } else {

                        Log.d("Запись в плейлист", "Добавлено  $playlistAdding")
                        val toastMessage = "Добавлено в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    }
                }
            }
        }
    }


    }

/* val favouritesIndicatorLiveData = playerViewModel.getFavouritesIndicator()
            favouritesIndicatorLiveData.observe(viewLifecycleOwner) { isFavourite ->
                if (isFavourite) {
                    binding.favorites.setImageResource(R.drawable.like_button)
                } else {
                    binding.favorites.setImageResource(R.drawable.buttonhert)
                }
            }*/


   /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow4.setOnClickListener {
            finish()
        }
        val track = intent.getParcelableExtra<Track>("track")

        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.time.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName ?: "Unknown Album"
        binding.year.text = (track?.releaseDate ?: "Year").take(4)
        binding.genre.text = track?.primaryGenreName ?: "Unknown Genre"
        binding.country.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        val radius = 8
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.placeholdermedia)
                .transform(RoundedCorners(radius))
                .into(binding.trackCover)
        }
        url = track?.previewUrl ?: return

        playerViewModel.createPlayer(url)

        binding.playButton.setOnClickListener {
            if (playerViewModel.getStateLiveData().value == PlayerState.STATE_PLAYING)
                playerViewModel.pause() else playerViewModel.play()
        }

        playerStateDrawer()

        playerViewModel.getTimeFromInteractor().observe(this) { timer ->
            binding.trackTimer.text = timer
            Log.d("время в активити", timer)
        }

        //нажатие на кнопку нравится
        binding.favorites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }

        playerViewModel.cliclFavourites(track).observe(this) { favourtitesIndicator ->
            if (favourtitesIndicator) {
                binding.favorites.setImageResource(R.drawable.like_button)
            } else binding.favorites.setImageResource(
                R.drawable.buttonhert
            )
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }

    private fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    fun playerStateDrawer() {
        playerViewModel.getStateLiveData().observe(this) {
            when (playerViewModel.getStateLiveData().value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }

                PlayerState.STATE_PREPARED -> {
                    preparePlayer()
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }

                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }
                else -> {

                }
            }
        }
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}*/



     
