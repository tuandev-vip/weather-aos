package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.source.weather.OpenMeteoWeatherRemoteDataSource
import com.tuan.weatherworld.data.source.weather.WeatherRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(
        implementation: OpenMeteoWeatherRemoteDataSource,
    ): WeatherRemoteDataSource
}
