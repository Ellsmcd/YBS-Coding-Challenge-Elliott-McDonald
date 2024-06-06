package com.elliott.ybscodingchallenge.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearchResponse
import com.elliott.ybscodingchallenge.data.searchapi.FlickrService
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.data.searchapi.TagMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchApi: FlickrService,
    private val photoRepository: PhotoRepository,
): ViewModel() {
    private var _state: MutableStateFlow<HomeViewModelState> = MutableStateFlow(HomeViewModelState())
    val state = _state.asStateFlow()

    init {
        getImages()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchTermAdded -> addSearchTerm(event.term)
            is HomeEvent.TagRemoved -> removeTag(event.tag)
            is HomeEvent.UserIdRemoved -> removeUserId()
            is HomeEvent.TextChanged -> updateSearchText(event.searchTerm)
            is HomeEvent.StrictSearchInteracted -> updateStrictSearch(event.enabled)
            is HomeEvent.PhotoTapped -> storePhotoDetail(event.photo)
            is HomeEvent.DetailScreenOpened -> _state.value = _state.value.copy(showDetailScreen = false)
            is HomeEvent.SomethingWentWrong -> getImages()
        }
    }

    private fun storePhotoDetail(photo: Photo) {
        photoRepository.setPhoto(photo)
        _state.value = _state.value.copy(showDetailScreen = true)
    }

    private fun addSearchTerm(term: String) {
        if (useRegex(term)) addUserId(term) else addTag(term)
    }

    private fun updateSearchText(text: String) {
        val sanitisedText = text.replace(" ", "")
        _state.value = _state.value.copy(searchInput = sanitisedText)
    }

    private fun addTag(tag: String) {
        val tags = _state.value.tags
        tags.add(tag)
        _state.value = _state.value.copy(tags = tags, searchInput = "")
        getImages()
    }

    private fun removeTag(tag: String) {
        val tags = _state.value.tags
        tags.remove(tag)
        _state.value = _state.value.copy(tags = tags)
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

    private fun useRegex(input: String): Boolean {
        val regex = Regex(pattern = "[0-9]+@N\\d\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }


    private fun getImages() {
        _state.value = _state.value.copy(flickrSearchResults = FlickrApiState.Loading)
        viewModelScope.launch {
            try {
                val response = searchApi.searchImages(
                    tags = _state.value.tags.joinToString(separator = ","),
                    tagMode = if (_state.value.strictSearch) TagMode.ALL else TagMode.ANY,
                    userId = _state.value.userId

                )
                _state.value =
                    _state.value.copy(flickrSearchResults = FlickrApiState.DataAvailable(response))
            } catch (error: Throwable) {
                _state.value = _state.value.copy(flickrSearchResults = FlickrApiState.Error(error))
        }
    }
    }
}

data class HomeViewModelState(
    val flickrSearchResults: FlickrApiState = FlickrApiState.Loading,
    val tags: MutableList<String> = mutableListOf("Yorkshire"),
    val userId: String? = null,
    val searchInput: String = "",
    val strictSearch: Boolean = false,
    val showDetailScreen: Boolean = false,
)

sealed class HomeEvent {
    data class TagRemoved(val tag: String): HomeEvent()
    data class SearchTermAdded(val term: String): HomeEvent()
    data object UserIdRemoved: HomeEvent()
    data class StrictSearchInteracted(val enabled: Boolean): HomeEvent()
    data class TextChanged(val searchTerm: String): HomeEvent()
    data class PhotoTapped(val photo: Photo): HomeEvent()
    data object DetailScreenOpened: HomeEvent()
    data object SomethingWentWrong: HomeEvent()
}

sealed class FlickrApiState {
    data object Loading: FlickrApiState()

    data class Error(val error: Throwable): FlickrApiState()

    data class DataAvailable(val response: FlickrSearchResponse?): FlickrApiState()

}