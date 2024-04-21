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
                favoritesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickAdapting(item: Track) {
        if (isClickAllowed) {
            favoritesViewModel.clickDebouncer()
            favoritesViewModel.addItem(item)
            val bundle = Bundle()
            bundle.putParcelable("track", item)
            val navController = findNavController()
            navController.navigate(R.id.action_favouritesFragment_to_playerFragment, bundle)
        }
    }

   /* private fun clickAdapting(item: Track) {
        favoritesViewModel.addItem(item)
        // Создаем и показываем PlayerFragment
        val playerFragment = PlayerFragment.newInstance(item)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, playerFragment)
            .addToBackStack(null)
            .commit()
    }*/
    /*private fun clickAdapting(item: Track) {
        favoritesViewModel.addItem(item)
        val intent = Intent(requireContext(), PlayerFragment::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
    }*/
    companion object {
        fun newInstance() = FavoritesFragment()

    }
}