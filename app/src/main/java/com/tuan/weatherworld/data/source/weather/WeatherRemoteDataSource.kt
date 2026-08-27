package com.tuan.weatherworld.data.source.weather

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto

interface WeatherRemoteDataSource {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): OpenMeteoForecastDto

    suspend fun searchLocations(query: String): OpenMeteoGeocodingResponseDto
}