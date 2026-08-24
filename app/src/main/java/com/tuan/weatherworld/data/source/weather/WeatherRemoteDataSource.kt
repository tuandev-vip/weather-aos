package com.tuan.weatherworld.data.source.weather

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto

interface WeatherRemoteDataSource {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): OpenMeteoForecastDto
}