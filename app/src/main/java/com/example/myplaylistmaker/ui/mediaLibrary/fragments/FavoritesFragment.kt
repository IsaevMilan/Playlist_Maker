package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import com.example.myplaylistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {
    private val favoritesViewModel by viewModel<FavouritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    private var isClickAllowed = true
    private val favoritesAdapter: TrackAdapter by lazy {
        TrackAdapter(
            clickListener = this::clickAdapting,
            longClickListener = {}
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        favoritesViewModel.loadFavourites()
    }

    private fun setupRecyclerView() {
        binding.favouritesRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private fun clickAdapting(item: Track) {
        if (isClickAllowed) {
            isClickAllowed = false // Блокируем повторные нажатия
            favoritesViewModel.addItem(item)
            val bundle = Bundle().apply { putParcelable("track", item) }
            findNavController().navigate(R.id.playerFragment, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFavourites()
        observeDestinationChanges()
    }

    private fun observeFavourites() {
        favoritesViewModel.favourites.observe(viewLifecycleOwner) { trackList ->
            binding.emptyMediaLibrary.visibility = if (trackList.isNullOrEmpty()) VISIBLE else GONE
            binding.emptyMediaLibraryText.visibility = if (trackList.isNullOrEmpty()) VISIBLE else GONE
            binding.favouritesRecycler.visibility = if (trackList.isNullOrEmpty()) GONE else VISIBLE
            favoritesAdapter.setItems(trackList)
        }
    }

    private fun observeDestinationChanges() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.playerFragment) {
                isClickAllowed = true // Разблокируем возможность нажатия
            }
        }
    }
}

/*
class FavoritesFragment : Fragment() {
    private val favoritesViewModel by viewModel<FavouritesViewModel>()
    private lateinit var nullableFavouritesBinding: FragmentFavoritesBinding

    private var isClickAllowed = true
    private val favoritesAdapter: TrackAdapter by lazy {
         TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = {})
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

        nullableFavouritesBinding.emptyMediaLibrary.visibility = GONE
        nullableFavouritesBinding.emptyMediaLibraryText.visibility = GONE

        nullableFavouritesBinding.favouritesRecycler.layoutManager = LinearLayoutManager(requireContext())
        nullableFavouritesBinding.favouritesRecycler.adapter = favoritesAdapter
        return nullableFavouritesBinding.root
    }

    private fun clickAdapting(item: Track) {
        if (isClickAllowed) {
            isClickAllowed = false // Блокируем повторные нажатия
            favoritesViewModel.addItem(item)
            val bundle = Bundle()
            bundle.putParcelable("track", item)
            val navController = findNavController()
            navController.navigate(R.id.playerFragment, bundle)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.favouritesMaker().observe(viewLifecycleOwner) {
                trackResultList ->
            if (favoritesViewModel.trackResultList.value.isNullOrEmpty()) {
                nullableFavouritesBinding.emptyMediaLibrary.visibility = VISIBLE
                nullableFavouritesBinding.emptyMediaLibraryText.visibility = VISIBLE
                nullableFavouritesBinding.favouritesRecycler.visibility=GONE
                favoritesAdapter.notifyDataSetChanged()
            } else {
                nullableFavouritesBinding.emptyMediaLibrary.visibility = GONE
                nullableFavouritesBinding.emptyMediaLibraryText.visibility = GONE
                nullableFavouritesBinding.favouritesRecycler.visibility=VISIBLE
                favoritesAdapter.setItems(favoritesViewModel.trackResultList.value!!)
            }
        }

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.playerFragment) {
                isClickAllowed = true // Разблокируем возможность нажатия
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()

    }
}*/
