package com.tuan.weatherworld.data.mapper


import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoLocationDto

/**
 * Chuyển kết quả tìm kiếm địa điểm của Open-Meteo từ DTO sang domain model.
 * Các trường mô tả riêng của API không cần cho UI sẽ không đi qua ranh giới này.
 */
fun OpenMeteoGeocodingResponseDto.toDomain(): List<WeatherLocation> {
    return results.map { locationDto ->
        locationDto.toDomain()
    }
}

private fun OpenMeteoLocationDto.toDomain(): WeatherLocation {
    return WeatherLocation(
        displayName = name.trim(),
        latitude = latitude,
        longitude = longitude,
    )
}
