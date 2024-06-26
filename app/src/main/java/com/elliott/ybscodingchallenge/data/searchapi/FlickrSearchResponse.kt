package com.elliott.ybscodingchallenge.data.searchapi

data class FlickrSearchResponse(
    val photos: Photos?,
    val stat: String
)

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<Photo>,
    val total: Int
)

data class Photo(
    val datetaken: String,
    val description: DescriptionContent,
    val farm: Int,
    val id: String,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String,
    val tags: String,
    val url_h: String? = null,
    val views: String
)

data class DescriptionContent(
    val _content: String
)