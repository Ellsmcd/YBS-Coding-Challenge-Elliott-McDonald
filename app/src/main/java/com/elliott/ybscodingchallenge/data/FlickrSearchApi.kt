package com.elliott.ybscodingchallenge.data



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
            @Query("api_key") apiKey: String = "40124db5b76259ab8e386feac0acfb81",
            @Query("safe_search") safeSearch: Int = 1,
            @Query("tags") tags: String = "Yorkshire",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
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
