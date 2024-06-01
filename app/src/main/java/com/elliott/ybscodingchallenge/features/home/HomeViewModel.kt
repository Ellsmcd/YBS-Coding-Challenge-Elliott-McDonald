package com.elliott.ybscodingchallenge.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliott.ybscodingchallenge.data.FlickrSearch
import com.elliott.ybscodingchallenge.data.FlickrSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchApi: FlickrSearch.Service
): ViewModel() {
    private var _state: MutableStateFlow<HomeViewModelState> = MutableStateFlow(HomeViewModelState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getImages()
        }
    }
    suspend fun getImages() {
        viewModelScope.launch {
            val response = searchApi.searchImages()
            _state.value = _state.value.copy(flickrSearchResults = response)
        }
    }
}

data class HomeViewModelState(
    val flickrSearchResults: FlickrSearchResponse? = null
)