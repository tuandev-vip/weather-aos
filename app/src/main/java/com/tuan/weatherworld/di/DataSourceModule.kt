package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.source.location.device.DeviceLocationProvider
import com.tuan.weatherworld.data.source.location.device.FusedDeviceLocationProvider
import com.tuan.weatherworld.data.source.location.name.AndroidLocationNameResolver
import com.tuan.weatherworld.data.source.location.name.LocationNameResolver
import com.tuan.weatherworld.data.source.weather.OpenMeteoWeatherRemoteDataSource
import com.tuan.weatherworld.data.source.weather.WeatherRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Nối từng abstraction data source với implementation production tương ứng.
 *
 * ViewModel/repository không biết Retrofit, Google Play Services hay Android
 * Geocoder cụ thể. Khi test có thể thay implementation bằng fake tại ranh giới này.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(
        implementation: OpenMeteoWeatherRemoteDataSource,
    ): WeatherRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDeviceLocationProvider(
        implementation: FusedDeviceLocationProvider,
    ): DeviceLocationProvider

    @Binds
    @Singleton
    abstract fun bindLocationNameResolver(
        implementation: AndroidLocationNameResolver,
    ): LocationNameResolver
}
