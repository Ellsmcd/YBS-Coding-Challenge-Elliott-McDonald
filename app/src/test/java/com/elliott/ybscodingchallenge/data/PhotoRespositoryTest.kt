package com.elliott.ybscodingchallenge.data

import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepositoryImpl
import com.elliott.ybscodingchallenge.data.searchapi.DescriptionContent
import com.elliott.ybscodingchallenge.data.searchapi.Photo
import org.junit.Assert
import org.junit.Test

class PhotoRespositoryTest {

    private lateinit var sut: PhotoRepository

    private fun initialiseSut() {
        sut = PhotoRepositoryImpl()
    }

    @Test
    fun testPhotoIsAddedWhenSetIsCalled() {
        initialiseSut()
        sut.setPhoto(photo)
        Assert.assertEquals(
            photo,
            sut.getPhoto()
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