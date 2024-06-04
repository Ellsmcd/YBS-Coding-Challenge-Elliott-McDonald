package com.elliott.ybscodingchallenge.data.searchapi


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

object FlickrSearch {
    interface Api {
        // Put some of this in constants
        @GET("/services/rest/")
        suspend fun searchImages(
            @Query("tags") tags: String,
            @Query("tag_mode") tagMode: String = "all",
            @Query("user_id") userId: String? = null,
            @Query("method") method: String = "flickr.photos.search",
            @Query("extras") extras: String = "description, date_taken, tags, url_h, views",
        ): Response<FlickrSearchResponse>
    }

    class Service @Inject constructor(
        private val api: Api,
        private val ioDispatcher: CoroutineDispatcher
    ) {
        suspend fun searchImages(
            tags: String,
            tagMode: TagMode,
            userId: String? = null,
        ): FlickrSearchResponse? {
            return withContext(ioDispatcher) {
                api.searchImages(
                    tags = tags,
                    tagMode = tagMode.name.lowercase(),
                    userId = userId
                ).body()
            }
        }
    }
}

enum class TagMode {
    ALL,
    ANY,
}
