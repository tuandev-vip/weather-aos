package com.tuan.weatherworld.data.source.weather

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto
import com.tuan.weatherworld.data.source.weather.remote.GeocodingApi
import com.tuan.weatherworld.data.source.weather.remote.WeatherApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source production giao tiếp với hai dịch vụ Open-Meteo qua Retrofit.
 *
 * Lớp chỉ nhận và trả DTO; việc chuyển sang domain model thuộc trách nhiệm của
 * repository/mapper. Retrofit không bị lộ lên ViewModel hoặc UI.
 */
@Singleton
class OpenMeteoWeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi,
) : WeatherRemoteDataSource {
    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): OpenMeteoForecastDto {
        return weatherApi.getForecast(
            latitude = latitude,
            longitude = longitude,
        )
    }

    override suspend fun searchLocations(
        query: String
    ): OpenMeteoGeocodingResponseDto {
        return geocodingApi.searchLocations(
            name = query,
        )
    }


}
