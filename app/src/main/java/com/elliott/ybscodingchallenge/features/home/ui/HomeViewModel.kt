package com.elliott.ybscodingchallenge.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearch
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearchResponse
import com.elliott.ybscodingchallenge.data.searchapi.TagMode
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
            is HomeEvent.UserIdAdded -> addUserId(event.userId)
            is HomeEvent.UserIdRemoved -> removeUserId()
            is HomeEvent.TextChanged -> updateSearchText(event.searchTerm)
            is HomeEvent.StrictSearchInteracted -> updateStrictSearch(event.enabled)
            is HomeEvent.SearchTypeChanged -> updateSearchType(event.userId)
        }
    }

    private fun updateSearchType(searchUsername: Boolean) {
        _state.value = _state.value.copy(searchUserId = searchUsername)
    }

    private fun updateSearchText(text: String) {
        _state.value = _state.value.copy(searchInput = text)
    }

    private fun addTag(tag: String) {
        val tags = _state.value.tags
        tags.add(tag)
        _state.value = _state.value.copy(tags = tags, searchInput = "")
        getImages()
    }

    private fun removeTag(tag: String) {
        val tags = _state.value.tags
        _state.value = _state.value.copy(tags = tags)
        tags.remove(tag)
        getImages()
    }

    private fun addUserId(userId: String) {
        _state.value = _state.value.copy(userId = userId)
        _state.value = _state.value.copy(searchInput = "")
        getImages()
    }

    private fun removeUserId() {
        _state.value = _state.value.copy(userId = null)
        getImages()
    }

    private fun updateStrictSearch(enabled: Boolean) {
        _state.value = _state.value.copy(strictSearch = enabled)
        getImages()
    }



    private fun getImages() {
        viewModelScope.launch {
            val response = searchApi.searchImages(
                tags = _state.value.tags.joinToString(separator = ","),
                tagMode = if (_state.value.strictSearch) TagMode.ALL else TagMode.ANY,
                userId = _state.value.userId

                )
            _state.value = _state.value.copy(flickrSearchResults = response)
        }
    }
}

data class HomeViewModelState constructor(
    val flickrSearchResults: FlickrSearchResponse? = null,
    val tags: MutableList<String> = mutableListOf("Yorkshire"),
    val userId: String? = null,
    val searchInput: String = "",
    val searchUserId: Boolean = false,
    val strictSearch: Boolean = false,
)

sealed class HomeEvent {
    data class TagAdded(val tag: String): HomeEvent()
    data class TagRemoved(val tag: String): HomeEvent()
    data class UserIdAdded(val userId: String): HomeEvent()
    data object UserIdRemoved: HomeEvent()
    data class StrictSearchInteracted(val enabled: Boolean): HomeEvent()
    data class TextChanged(val searchTerm: String): HomeEvent()
    data class SearchTypeChanged(val userId: Boolean): HomeEvent()
}