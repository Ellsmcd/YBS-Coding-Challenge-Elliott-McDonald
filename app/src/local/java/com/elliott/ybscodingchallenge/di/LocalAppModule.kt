package com.elliott.ybscodingchallenge.di

import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepositoryImpl
import com.elliott.ybscodingchallenge.data.searchapi.FlickrService
import com.elliott.ybscodingchallenge.data.searchapi.FlickrServiceLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalAppModule {
    @Singleton
    @Provides
    internal fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    internal fun providePhotoRepository(): PhotoRepository = PhotoRepositoryImpl()

    @Singleton
    @Provides
    internal fun provideFlickrService(
        ioDispatcher: CoroutineDispatcher
    ): FlickrService {
        return FlickrServiceLocal(
            ioDispatcher
        )
    }
}