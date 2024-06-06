package com.elliott.ybscodingchallenge.features.home.ui

import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepositoryImpl
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearch
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearchResponse
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import com.elliott.ybscodingchallenge.data.searchapi.Photos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var sut: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockFlickrService: FlickrSearch.Service = mock {
            onBlocking { searchImages(any(), any(), anyOrNull()) } doReturn(flickrSearchResponse)
        }

    private val photoRepository: PhotoRepository = PhotoRepositoryImpl()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun initialiseSut() {
        sut = HomeViewModel(
            mockFlickrService,
            photoRepository
        )
    }

    @Test
    fun testSearchApiIsCalledOnInitialisation() {
        initialiseSut()
        runTest {
            assertEquals(
                FlickrApiState.DataAvailable(flickrSearchResponse),
                sut.state.value.flickrSearchResults,
            )
        }
    }

    @Test
    fun testSearchApiReturnsErrorWhenApiFails() {
        val mockThrowingFlickrService: FlickrSearch.Service = mock {
            onBlocking { searchImages(any(), any(), anyOrNull()) } doThrow(RuntimeException())
        }
        sut = HomeViewModel(
            mockThrowingFlickrService,
            photoRepository,
        )
        runTest {
            assertEquals(
                FlickrApiState.Error(RuntimeException()).toString(),
                sut.state.value.flickrSearchResults.toString()
            )
        }
    }

    @Test
    fun testSearchTermAddedAddsTagToStateAndCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.SearchTermAdded("newTag"))
            assertEquals(
                mutableListOf("Yorkshire", "newTag"),
                sut.state.value.tags
                )
            verify(mockFlickrService, times(2)).searchImages(any(), any(), anyOrNull())
        }
    }

    @Test
    fun testSearchTermAddedAddsUserIdToStateAndCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.SearchTermAdded("12345678@N04"))
            assertEquals(
                mutableListOf("Yorkshire"),
                sut.state.value.tags
            )
            assertEquals(
                "12345678@N04",
                sut.state.value.userId
            )
            sut.onEvent(HomeEvent.SearchTermAdded("12345@N04"))
            assertEquals(
                "12345@N04",
                sut.state.value.userId
            )
            verify(mockFlickrService, times(3)).searchImages(any(), any(), anyOrNull())
        }
    }

    @Test
    fun testTagRemovedRemovesTagFromStateAndCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.TagRemoved("Yorkshire"))
            assertEquals(
                mutableListOf<String>(),
                sut.state.value.tags
            )
            verify(mockFlickrService, times(2)).searchImages(any(), any(), anyOrNull())
        }
    }

    @Test
    fun testSearchTermAddedRemovesUserIdFromStateAndCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.SearchTermAdded("12345678@N04"))
            assertEquals(
                "12345678@N04",
                sut.state.value.userId
            )
            sut.onEvent(HomeEvent.UserIdRemoved)
            assertEquals(
                null,
                sut.state.value.userId
            )
            verify(mockFlickrService, times(3)).searchImages(any(), any(), anyOrNull())
        }
    }

    @Test
    fun testTextChangedUpdatesStateAndRemovesSpaces() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.TextChanged("Text with spaces"))
            assertEquals(
                "Textwithspaces",
                sut.state.value.searchInput
            )
        }
    }

    @Test
    fun testStrictSearchInteractedUpdatesStateAndCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.StrictSearchInteracted(true))
            assertEquals(
                true,
                sut.state.value.strictSearch
            )
            sut.onEvent(HomeEvent.StrictSearchInteracted(false))
            assertEquals(
                false,
                sut.state.value.strictSearch
            )
            verify(mockFlickrService, times(3)).searchImages(any(), any(), anyOrNull())
        }
    }

    @Test
    fun testPhotoTappedUpdatesStateAndCallsSavesPhotoToRepository() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.PhotoTapped(photo))
            assertEquals(
                true,
                sut.state.value.showDetailScreen
            )
            assertEquals(
                photo,
                photoRepository.getPhoto()
            )
        }
    }

    @Test
    fun testDetailScreenOpenedUpdatesState() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.PhotoTapped(photo))
            assertEquals(
                true,
                sut.state.value.showDetailScreen
            )
            sut.onEvent(HomeEvent.DetailScreenOpened)
            assertEquals(
                false,
                sut.state.value.showDetailScreen
            )
        }
    }

    @Test
    fun testSomethingWentWrongCallsApi() {
        initialiseSut()
        runTest {
            sut.onEvent(HomeEvent.SomethingWentWrong)
            verify(mockFlickrService, times(2)).searchImages(any(), any(), anyOrNull())
        }
    }

    companion object {
        private val photo = Photo(
            datetaken = "2024-04-21 14:11:00",
            description = DescriptionContent("A happy cow that I met on a walk"),
            farm = 0,
            id = "",
            isfamily = 0,
            isfriend = 0,
            ispublic = 0,
            owner = "",
            secret = "",
            server = "",
            title = "Picture of a happy cow!",
            tags = "Yorkshire",
            views = "100"
        )

        private val flickrSearchResponse = FlickrSearchResponse(
            Photos(
                1,
                1,
                100,
                listOf(
                    photo
                ),
                total = 100,
            ),
            stat = "200"
        )
    }
}