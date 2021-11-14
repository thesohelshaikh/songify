package com.thesohelshaikh.songify.di

import com.thesohelshaikh.songify.network.SongService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideSongService(): SongService {
        return SongService.create()
    }
}