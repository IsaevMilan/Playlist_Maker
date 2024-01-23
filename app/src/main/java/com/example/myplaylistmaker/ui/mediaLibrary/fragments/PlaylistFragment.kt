package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentNewPlaylistBinding
import com.example.myplaylistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentNewPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullablePlaylistBinding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = VISIBLE

        //кнопка создать плейлист
        nullablePlaylistBinding.newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.newPlaylistFragment)
        }

        //список плейлистов
        val recyclerView = nullablePlaylistBinding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter= playlistViewModel.playlistList.value?.let { PlaylistAdapter(it, {}) }
        if (playlistViewModel.playlistList.value.isNullOrEmpty()) nullablePlaylistBinding.playlistList.visibility=GONE

        nullablePlaylistBinding.playlistList.visibility=VISIBLE
        return nullablePlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistViewModel.playlistMaker().value.isNullOrEmpty()) {
                noPlaylist()
                return@observe
            } else {
                nullablePlaylistBinding.playlistList.adapter=PlaylistAdapter(playlistList) {}
                existPlaylist()
                return@observe
            }
        }
    }

    private fun noPlaylist(){
        nullablePlaylistBinding.emptyPlaylist.visibility=VISIBLE
        nullablePlaylistBinding.emptyPlaylistText.visibility=VISIBLE
        nullablePlaylistBinding.playlistList.visibility=GONE
    }

    private fun existPlaylist(){
        nullablePlaylistBinding.emptyPlaylist.visibility=GONE
        nullablePlaylistBinding.emptyPlaylistText.visibility=GONE
        nullablePlaylistBinding.playlistList.visibility=VISIBLE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}