package com.elliott.ybscodingchallenge.di

import com.elliott.ybscodingchallenge.data.FlickrSearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory
                    .create()
                    .asLenient()
                    .withNullSerialization()
            )
            .baseUrl("https://api.flickr.com/services")
            .build()
    }

    @Singleton
    @Provides
    fun provideFlickrSearchApi(retrofit: Retrofit): FlickrSearch.Api {
        return retrofit.create(FlickrSearch.Api::class.java)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}