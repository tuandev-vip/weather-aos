package com.tuan.weatherworld.data.mapper


import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto
import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoLocationDto

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
