package com.tuan.weatherworld.data.source.weather.remote

import com.tuan.weatherworld.data.source.weather.dto.OpenMeteoGeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Hợp đồng Retrofit cho tìm kiếm thuận: tên địa điểm -> danh sách tọa độ.
 *
 * API này phục vụ màn tìm kiếm. Nó khác với
 * [com.tuan.weatherworld.data.source.location.name.LocationNameResolver], nơi đổi
 * tọa độ GPS thành tên địa điểm bằng Android Geocoder.
 */
interface GeocodingApi {

    @GET("v1/search")
    suspend fun searchLocations(
        @Query("name")
        name: String,

        @Query("count")
        count: Int = 10,

        @Query("language")
        language: String = "vi",

        @Query("format")
        format: String = "json",

        @Query("countryCode")
        countryCode: String? = null,
    ): OpenMeteoGeocodingResponseDto
}
