package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.repository.SavedLocationRepository
import com.tuan.weatherworld.data.repository.SavedLocationRepositoryImpl
import com.tuan.weatherworld.data.repository.SettingsRepository
import com.tuan.weatherworld.data.repository.SettingsRepositoryImpl
import com.tuan.weatherworld.data.repository.WeatherRepository
import com.tuan.weatherworld.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Chọn implementation production cho các repository dạng interface.
 *
 * Module là abstract và dùng `@Binds` vì Hilt chỉ cần biết quan hệ
 * interface -> implementation; không có logic khởi tạo thủ công để thực thi.
 */
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

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        implementation: SettingsRepositoryImpl,
    ): SettingsRepository
}
