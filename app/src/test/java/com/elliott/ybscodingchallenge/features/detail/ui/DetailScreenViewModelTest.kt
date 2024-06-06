package com.elliott.ybscodingchallenge.features.detail.ui

import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepositoryImpl
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailScreenViewModelTest {
    private lateinit var sut: DetailScreenViewModel

    private val photoRepository: PhotoRepository = PhotoRepositoryImpl()

    private fun initialiseSut() {
        sut = DetailScreenViewModel(photoRepository)
    }

    @Test
    fun testPhotoIsFetchedWhenViewModelIsInitialised() {
        photoRepository.setPhoto(photo)
        initialiseSut()
        assertEquals(
            photo,
            sut.state
        )
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
    }
}