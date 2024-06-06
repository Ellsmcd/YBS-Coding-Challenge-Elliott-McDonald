package com.elliott.ybscodingchallenge.features.detail.ui

import androidx.lifecycle.ViewModel
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    photoRepository: PhotoRepository
): ViewModel() {
    val state = photoRepository.getPhoto()
}