package com.elliott.ybscodingchallenge.data.searchapi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FlickrServiceLocal @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
): FlickrService {
    override suspend fun searchImages(
        tags: String,
        tagMode: TagMode,
        userId: String?,
    ): FlickrSearchResponse? {
        return withContext(ioDispatcher) {
            delay(500)
            if ("throwError" in tags) {
                throw Exception("Local error")
            } else if ("failResponse" in tags || tags.isBlank()) {
                emptyResponse
            } else {
                flickrSearchResponse
            }
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
            owner = "12345678@N04",
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
                    photo,
                    photo,
                    photo,
                    photo
                ),
                total = 100,
            ),
            stat = "200"
        )
        private val emptyResponse = FlickrSearchResponse(
            Photos(
                1,
                1,
                100,
                listOf(),
                total = 0,
            ),
            stat = "fail"
        )
    }
}
