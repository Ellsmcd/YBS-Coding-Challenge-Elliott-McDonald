package com.elliott.ybscodingchallenge.di

import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepository
import com.elliott.ybscodingchallenge.data.photorepository.PhotoRepositoryImpl
import com.elliott.ybscodingchallenge.data.searchapi.FlickrSearch
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url = chain
                    .request()
                    .url
                    .newBuilder()
                    .addQueryParameter("api_key", "40124db5b76259ab8e386feac0acfb81")
                    .addQueryParameter("safe_search", "1")
                    .addQueryParameter("format", "json")
                    .addQueryParameter("nojsoncallback", "1")
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build()
    }
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory
                    .create(moshi)
                    .asLenient()
                    .withNullSerialization()
            )
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

    @Singleton
    @Provides
    fun providePhotoRepository(): PhotoRepository = PhotoRepositoryImpl()
}