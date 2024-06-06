package com.elliott.ybscodingchallenge.data.photorepository

import com.elliott.ybscodingchallenge.data.searchapi.Photo

interface PhotoRepository {
    fun getPhoto(): Photo
    fun setPhoto(photo: Photo)
}
class PhotoRepositoryImpl: PhotoRepository {
    private lateinit var photoStore: Photo

    override fun getPhoto(): Photo = photoStore

    override fun setPhoto(photo: Photo) {
        photoStore = photo
    }
}