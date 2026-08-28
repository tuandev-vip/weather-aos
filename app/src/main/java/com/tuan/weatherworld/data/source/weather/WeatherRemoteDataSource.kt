package com.tuan.weatherworld.data.source.weather

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoForecastDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto

/**
 * Ranh giới dữ liệu mạng mà repository phụ thuộc vào.
 *
 * Repository chỉ biết interface này và DTO, không gọi Retrofit API trực tiếp.
 * Nhờ vậy implementation thật có thể được thay bằng fake trong unit test.
 */
interface WeatherRemoteDataSource {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): OpenMeteoForecastDto

    suspend fun searchLocations(query: String): OpenMeteoGeocodingResponseDto
}
