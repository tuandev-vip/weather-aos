package com.tuan.weatherworld.data.mapper

import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.location.local.entity.SavedLocationEntity

fun SavedLocationEntity.toDomain(): WeatherLocation {
    return WeatherLocation(
        displayName = displayName,
        latitude = latitude,
        longitude = longitude,
    )
}

fun WeatherLocation.toEntity(): SavedLocationEntity {
    return SavedLocationEntity(
        displayName = displayName,
        latitude = latitude,
        longitude = longitude,
    )
}