package com.tuan.weatherworld.data.mapper

import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.data.source.location.local.entity.SavedLocationEntity

/** Chuyển một hàng Room sang model địa điểm mà ViewModel và UI sử dụng. */
fun SavedLocationEntity.toDomain(): WeatherLocation {
    return WeatherLocation(
        displayName = displayName,
        latitude = latitude,
        longitude = longitude,
    )
}

/** Chuyển domain model sang entity trước khi ghi vào bảng `saved_locations`. */
fun WeatherLocation.toEntity(): SavedLocationEntity {
    return SavedLocationEntity(
        displayName = displayName,
        latitude = latitude,
        longitude = longitude,
    )
}
