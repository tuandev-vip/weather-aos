package com.tuan.weatherworld.data.source.weather.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Cấu trúc JSON trả về từ Open-Meteo Geocoding API khi tìm theo tên địa điểm.
 * DTO giữ sát hợp đồng server; mapper chỉ lấy tên và tọa độ cần thiết sang domain.
 */
@Serializable
data class OpenMeteoGeocodingResponseDto(
    val results: List<OpenMeteoLocationDto> = emptyList(),
)

@Serializable
data class OpenMeteoLocationDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,

    @SerialName("country_code")
    val countryCode: String? = null,

    @SerialName("feature_code")
    val featureCode: String? = null,

    val country: String? = null,
    val admin1: String? = null,
    val admin2: String? = null,
    val admin3: String? = null,
    val admin4: String? = null,
    val timezone: String? = null,
)
