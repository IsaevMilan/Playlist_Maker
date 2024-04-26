package com.example.myplaylistmaker.ui.tracks_in_playlist.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentPlaylistEditorBinding
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels.PlayListEditorViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlayListEditor : Fragment() {
    private lateinit var playlistEditorBinding: FragmentPlaylistEditorBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private val viewModel: PlayListEditorViewModel by viewModel()
    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistEditorBinding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE


        return playlistEditorBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        val name = playlistEditorBinding.playlistNameEditText
        if (playlist != null) {
            name.setText(playlist.playlistName)
        }
        if (playlist != null) {
            playlistEditorBinding.playlistDescription.setText(playlist.description)
        }
        ///обложка
        val baseWidth = 312
        val baseHeight = 312
        val getImage = (playlist?.uri ?: "Unknown Cover")

        if (getImage != "Unknown Cover") {
            playlistEditorBinding.playlistPlaceHolder.visibility = View.GONE
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .placeholder(R.drawable.placeholder)
                .override(baseWidth, baseHeight)
                .into(playlistEditorBinding.playlistPic)
            selectedUri = getImage.toUri()
        }
        val rxPermissions = RxPermissions(this)

        //отработка на кнопку назад
        playlistEditorBinding.backButton.setOnClickListener {
            closer()
        }

        //устанавливаем цвет кнопки "Создать"
        turnOnCreateButton()
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    turnOffCreateButton()
                } else {
                    turnOnCreateButton()
                }
            }
        }
        playlistEditorBinding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)

        //переменеая с лямбдой, которая берет изображение и сохраняет его в ханилище
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val radius = 8
                    val width = 312
                    val height = 312
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_picture)
                        .transform(CenterCrop(), RoundedCorners(radius))
                        .override(width, height)
                        .into(playlistEditorBinding.playlistPic)
                    saveImageToPrivateStorage(uri)

                } else {
                    //ничего не делаем
                }
            }

        //обработка нажатия на область обложки
        playlistEditorBinding.playlistPic.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        // Пользователь отказал, ничего не делаем
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closer()}

        playlistEditorBinding.saveButton.setOnClickListener {
            if (playlistEditorBinding.playlistNameEditText.text.toString()
                    .isEmpty()
            ) return@setOnClickListener
            if (playlist != null) {
                savePlaylist(playlist)
            }
        }

    }

    private fun closer() {
        val fragmentmanager = requireActivity().supportFragmentManager
        fragmentmanager.popBackStack()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover_${fileCount + 1}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        playlistEditorBinding.playlistPlaceHolder.visibility = View.GONE
        selectedUri = file.toUri()
    }

    private fun turnOffCreateButton() {
        playlistEditorBinding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.thumbSwitchDay))
        playlistEditorBinding.saveButton.isEnabled = false
    }

    private fun turnOnCreateButton() {
        playlistEditorBinding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.ypBlue))
        playlistEditorBinding.saveButton.isEnabled = true
    }

    private fun savePlaylist(playlist: Playlist) {
        viewModel.savePlayList(
            playlist,
            playlistEditorBinding.playlistNameEditText.text.toString(),
            playlistEditorBinding.playlistDescription.text.toString(),
            selectedUri.toString(),
        )
        closer()
    }
}