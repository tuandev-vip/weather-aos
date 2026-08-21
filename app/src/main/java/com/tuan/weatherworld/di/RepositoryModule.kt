package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.repository.WeatherRepository
import com.tuan.weatherworld.data.repository.mock.MockWeatherRepository
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
        implementation: MockWeatherRepository,
    ): WeatherRepository
}
