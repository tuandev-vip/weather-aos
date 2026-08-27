package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.SavedLocationRepositoryImpl
import com.tuan.weatherworld.data.repository.WeatherRepository
import com.tuan.weatherworld.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        implementation: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindSavedLocationRepository(
        implementation: SavedLocationRepositoryImpl
    ): SavedLocationRepository
}
