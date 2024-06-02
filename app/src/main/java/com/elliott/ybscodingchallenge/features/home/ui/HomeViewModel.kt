package com.elliott.ybscodingchallenge.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearch
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearchResponse
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.TagAdded -> addTag(event.tag)
            is HomeEvent.TagRemoved -> removeTag(event.tag)
        }
    }

    private fun addTag(tag: String) {
        val tags = _state.value.tags
        tags.add(tag)
        _state.value = _state.value.copy(tags = tags)
        getImages()
    }

    private fun removeTag(tag: String) {
        val tags = _state.value.tags
        tags.remove(tag)
        _state.value = _state.value.copy(tags = tags)
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            val response = searchApi.searchImages(_state.value.tags.joinToString(separator = ","))
            _state.value = _state.value.copy(flickrSearchResults = response)
        }
    }
}

data class HomeViewModelState(
    val flickrSearchResults: FlickrSearchResponse? = null,
    val tags: MutableList<String> = mutableListOf("Yorkshire")
)

sealed class HomeEvent {
    data class TagAdded(val tag: String): HomeEvent()
    data class TagRemoved(val tag: String): HomeEvent()
}