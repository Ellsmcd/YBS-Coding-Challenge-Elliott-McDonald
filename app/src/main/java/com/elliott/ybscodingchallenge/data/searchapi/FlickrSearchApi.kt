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
            @Query("method") method: String = "flickr.photos.search",
            @Query("tags") tags: String = "Yorkshire",
        ): Response<FlickrSearchResponse>
    }

    class Service @Inject constructor(
        private val api: Api,
        private val ioDispatcher: CoroutineDispatcher
    ) {
        suspend fun searchImages(): FlickrSearchResponse? {
            return withContext(ioDispatcher) {
                api.searchImages().body()
            }
        }
    }
}